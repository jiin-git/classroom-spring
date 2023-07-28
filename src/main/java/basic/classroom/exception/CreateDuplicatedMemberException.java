package basic.classroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "중복 가입 ID 입니다. 다시 가입해주세요.")
public class CreateDuplicatedMemberException extends RuntimeException{
    public CreateDuplicatedMemberException() {
        super();
    }

    public CreateDuplicatedMemberException(String message) {
        super(message);
    }

    public CreateDuplicatedMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateDuplicatedMemberException(Throwable cause) {
        super(cause);
    }

    protected CreateDuplicatedMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
