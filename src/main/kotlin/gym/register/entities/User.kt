package gym.register.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Setter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Setter
@Entity
@Table(name = "TB_USERS")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var idKey: Int = 0

    @Column
    var id: String = ""

    @Column(unique = true)
    var username = ""

    @Column
    var cellphoneNumber = ""

    @Column
    var password = ""
        @JsonIgnore
        get
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }
}