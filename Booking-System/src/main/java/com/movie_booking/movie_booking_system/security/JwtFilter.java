package com.movie_booking.movie_booking_system.security;

import com.movie_booking.movie_booking_system.service.CustomUserDetailsService;
import com.movie_booking.movie_booking_system.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header=request.getHeader("Authorization");
        String token=null;
        String username=null;

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Get JWT from Authorization header
        if(header!=null && header.startsWith("Bearer ")){
            token=header.substring(7);
            try{
                // 2. Extract username/email from JWT
                username= jwtService.extractUsername(token);
            }
            catch (Exception e){
                // Invalid JWT
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 3. If username exists and user is not already authenticated
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            try {
                // 4. Get user details from database
                UserDetails userDetails= userDetailsService.loadUserByUsername(username);

                // 5. Validate JWT
                if(jwtService.isTokenValid(token, userDetails)){
                    // 6. Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 7. Attach request details
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 8. Tell Spring Security that user is authenticated
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
            catch (Exception e) {
                // Invalid token / user not found
                SecurityContextHolder.clearContext();
            }
        }

        // 9. Continue to controller
        filterChain.doFilter(request, response);
    }
}
