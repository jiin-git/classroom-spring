package basic.classroom.exception;

import lombok.Getter;

@Getter
public class InvalidMemberStatusException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidMemberStatusException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
