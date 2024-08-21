package org.example.jwtauth.securityConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jwtauth.service.JWTokenProviderService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * Authenticate a user with a generated jwt token
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTokenProviderService jwTokenProvider;

    private final UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JWTokenProviderService jwTokenProvider, UserDetailsService userDetailsService) {
        this.jwTokenProvider = jwTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //step`1: get the token with a  bearer schema.
        String getBearerTokenName = getAuthorizedToken(request);

        if (StringUtils.hasText(getBearerTokenName) && jwTokenProvider.validateToken(getBearerTokenName)) {
            String username = jwTokenProvider.getUsernameFromToken(getBearerTokenName);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            context.setAuthentication(authenticationToken);

            SecurityContextHolder.setContext(context);

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException | AuthenticationException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "access Denied");
        }
    }

    private String getAuthorizedToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);

        }

        return null;
    }
}
