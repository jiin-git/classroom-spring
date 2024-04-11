package basic.classroom.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    public MemberNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
