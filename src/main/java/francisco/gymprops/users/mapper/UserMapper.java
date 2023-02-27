package francisco.gymprops.users.mapper;


import francisco.gymprops.register.dto.RegisterDTO;
import francisco.gymprops.users.dto.UserDTO;
import francisco.gymprops.users.entity.User;

public interface UserMapper {

    UserDTO toUserDto(User user);
    User registerUser(RegisterDTO registerDTO);

}
