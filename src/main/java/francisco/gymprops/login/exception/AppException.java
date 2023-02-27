package francisco.gymprops.login.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    public AppException(String userNotFound, HttpStatus notFound) {
    }
}
