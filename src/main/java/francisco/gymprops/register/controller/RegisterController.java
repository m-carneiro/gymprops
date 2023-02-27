package francisco.gymprops.register.controller;


import francisco.gymprops.register.dto.RegisterDTO;
import francisco.gymprops.users.entity.User;
import francisco.gymprops.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private UserService registerService;

    public RegisterController(UserService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO body) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(body.getLogin());
        user.setCellphone(body.getCellphone());
        user.setPassword(Arrays.toString(body.getPassword()));

        return ResponseEntity.ok(this.registerService.save(user));
    }
}

