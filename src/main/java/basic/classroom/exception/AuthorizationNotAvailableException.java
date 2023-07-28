package basic.classroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "허가되지 않은 계정 접근이 확인되어 접속이 제한됩니다.")
public class AuthorizationNotAvailableException extends RuntimeException{
    public AuthorizationNotAvailableException() {
        super();
    }

    public AuthorizationNotAvailableException(String message) {
        super(message);
    }

    public AuthorizationNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationNotAvailableException(Throwable cause) {
        super(cause);
    }

    protected AuthorizationNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
