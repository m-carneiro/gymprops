package francisco.gymprops.login.authentication.service;

import francisco.gymprops.login.dto.LoginDTO;
import francisco.gymprops.login.exception.AppException;
import francisco.gymprops.users.dto.UserDTO;
import francisco.gymprops.users.entity.User;
import francisco.gymprops.users.mapper.UserMapper;
import francisco.gymprops.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String ALGORITHM_VALUE = "HmacSH512";

    @Transactional
    public UserDTO authenticate(LoginDTO credentials) {
        User user = userRepository.getByUsername(credentials.getUsername())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentials.getPassword()), user.getPassword())) {
            log.debug("User {} authenticated correctly", credentials.getUsername());
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDTO findByLogin(String username) {
        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new AppException("Login not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public String createToken(UserDTO userDTO) {
        return userDTO.getId() + "&" + userDTO.getLogin() + "&" + calculateHmac(userDTO);
    }

    public UserDTO findByToken(String token) {
        String[] parts = token.split("&");

        Long userID = Long.valueOf(parts[0]);
        String username = parts[1];
        String hmac = parts[2];

        UserDTO user = findByLogin(username);

        if(!hmac.equals(calculateHmac(user))
                        ||
           !userID.equals(user.getId())) {
            throw new RuntimeException("Invalid cookie value!");
        }

        return user;
    }

    private String calculateHmac(UserDTO userDTO) {
        byte[] secretKeyBytes = Objects.requireNonNull(userDTO.getToken())
                .getBytes(StandardCharsets.UTF_8);

        byte[] valueBytes = Objects.requireNonNull(userDTO.getId() + "&" + userDTO.getLogin())
                .getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance(ALGORITHM_VALUE);
            SecretKeySpec SKS = new SecretKeySpec(secretKeyBytes, ALGORITHM_VALUE);
            mac.init(SKS);

            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
