package gym.login.service

import gym.register.entities.User
import gym.register.repositories.UserRegisterRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(
    private var userRegisterRepository: UserRegisterRepository
) {

    fun getByUsername(username: String): User? {
        return userRegisterRepository.getByUsername(username)
    }

    fun getByID(id: String): Optional<User> {
        return userRegisterRepository.findById(id)
    }

    fun doOnLogin(username: String, access: String): Boolean {
        val userInDataBase = userRegisterRepository.getByUsername(username)

        return if(userInDataBase!!.id.isNotEmpty()) {
            userInDataBase.password == access
        } else {
            false
        }
    }
}