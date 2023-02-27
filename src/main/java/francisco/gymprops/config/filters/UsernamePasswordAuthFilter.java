package francisco.gymprops.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import francisco.gymprops.login.dto.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String LOGIN_URL = "/api/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(LOGIN_URL.equals(request.getServletPath())
        && HttpMethod.POST.matches(request.getMethod())) {
            LoginDTO loginDTO = MAPPER.readValue(request.getInputStream(), LoginDTO.class);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                               loginDTO.getUsername(),
                               loginDTO.getPassword()
                    )
            );
        }

        filterChain.doFilter(request, response);
    }
}
