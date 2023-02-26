package gym.register.services

import gym.register.entities.User
import gym.register.repositories.UserRegisterRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class RegisterService(
    private var userRegisterRepository: UserRegisterRepository
) {

    fun save(user: User): User {
        return this.userRegisterRepository.save(user)
    }

    fun getUserRegistry(user: User): Boolean {
        return try {
            val isUserExistent = userRegisterRepository.findById(user.id)
            !isUserExistent.isEmpty
        } catch (e: RuntimeException) {
            println(e.stackTrace)
            false
        }
    }
}