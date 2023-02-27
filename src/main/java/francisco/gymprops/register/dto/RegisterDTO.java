package francisco.gymprops.register.dto;

import lombok.*;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {

    private String firstName;

    private String lastName;
    private String cellphone;

    private String login;

    private char[] password;
}
