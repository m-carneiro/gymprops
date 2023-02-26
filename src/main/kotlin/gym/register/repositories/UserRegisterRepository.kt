package gym.register.repositories

import gym.register.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRegisterRepository : JpaRepository<User, String> {
    fun getByUsername(username: String): User
}