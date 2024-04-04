package basic.classroom.exception;

import lombok.Getter;

@Getter
public class AuthorizationNotAvailableException extends RuntimeException{
    private ErrorCode errorCode;

    public AuthorizationNotAvailableException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
