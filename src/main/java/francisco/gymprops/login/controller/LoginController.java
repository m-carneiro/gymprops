package francisco.gymprops.login.controller;

import francisco.gymprops.config.filters.CookieAuthenticationFilter;
import francisco.gymprops.login.authentication.service.AuthenticationService;
import francisco.gymprops.users.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> signIn(
            @AuthenticationPrincipal UserDTO userDTO,
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME,
                authenticationService.createToken(userDTO));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Duration.of(1, ChronoUnit.DAYS).toSecondsPart());
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> signOut(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> CookieAuthenticationFilter.COOKIE_NAME
                        .equals(cookie.getName()))
                .findFirst();

        authCookie.ifPresent(cookie -> cookie.setMaxAge(0));

        return ResponseEntity.noContent().build();
    }
}
