package gym.login.controller

import gym.dto.Message
import gym.login.dto.LoginDTO
import gym.login.service.LoginService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class LoginController(
    private var loginService: LoginService
) {

    @PostMapping("/login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.loginService.getByUsername(body.username)
            ?: return ResponseEntity.badRequest().body(Message(INVALID_LOGIN_MESSAGE))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message(INVALID_LOGIN_MESSAGE))
        }

        val jwt = Jwts.builder()
            .setIssuer(user.id)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, ENCODER_KEY).compact()

        val cookie = Cookie(JWT_VALUE, jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message(SUCCESS_MESSAGE))
    }

    @GetMapping("/user")
    fun user(@CookieValue(JWT_VALUE) jwt: String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(NOT_AUTHENTICATED_MESSAGE))
            }

            val body = Jwts.parser().setSigningKey(ENCODER_KEY).parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.loginService.getByID(body.issuer.toString()))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Message(NOT_AUTHENTICATED_MESSAGE))
        }
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie(JWT_VALUE, "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message(SUCCESS_MESSAGE))
    }

    companion object {
        const val JWT_VALUE: String = "jwt"
        const val ENCODER_KEY: String = "secret"
        const val INVALID_LOGIN_MESSAGE: String = "Invalid credentials!"
        const val SUCCESS_MESSAGE: String = "Success!"
        const val NOT_AUTHENTICATED_MESSAGE: String = "Unauthenticated!"
    }
}