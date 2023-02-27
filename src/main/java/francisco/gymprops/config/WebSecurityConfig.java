package francisco.gymprops.config;


import francisco.gymprops.config.entrypoint.UserAuthenticationEntryPoint;
import francisco.gymprops.config.filters.CookieAuthenticationFilter;
import francisco.gymprops.config.filters.UsernamePasswordAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration

public class WebSecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    public WebSecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .exceptionHandling()
                    .authenticationEntryPoint(userAuthenticationEntryPoint)
                    .and()

                    .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new CookieAuthenticationFilter(), UsernamePasswordAuthFilter.class)

                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()

                    .logout()
                    .deleteCookies(CookieAuthenticationFilter.COOKIE_NAME)
                    .and()

                    .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll()
                    .anyRequest().authenticated();

        return httpSecurity.build();
    }
}
