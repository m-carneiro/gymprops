package gym.register.controllers

import gym.register.dto.RegisterDTO
import gym.register.entities.User
import gym.register.services.RegisterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
class RegisterController(
    private var registerService: RegisterService
) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User> {
        val user = User()
        user.id = UUID.randomUUID().toString()
        user.username = body.name
        user.cellphoneNumber = body.cellphone
        user.password = body.password

        return ResponseEntity.ok(this.registerService.save(user))
    }
}