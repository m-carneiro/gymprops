package gym.register.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.Setter

@Setter
@Entity
data class User(

    @Id
    var id: String,
    var username: String,
    var password: String
)