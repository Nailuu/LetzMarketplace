package lu.letzmarketplace.restapi.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lu.letzmarketplace.restapi.models.User;
import lu.letzmarketplace.restapi.repositories.UserRepository;
import lu.letzmarketplace.restapi.services.CustomUserDetailsService;
import lu.letzmarketplace.restapi.services.JWTService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@ConditionalOnBean(JWTService.class) // avoid loading this class while using web mvc mock
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            String email = jwtService.extractEmail(token);
            String type = jwtService.extractType(token);

            boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() != null;
            if (email != null && type != null && type.equals("access") && !isAuthenticated) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                User user = userRepository.findByEmail(userDetails.getUsername())
                        .orElse(null);

                if (jwtService.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
