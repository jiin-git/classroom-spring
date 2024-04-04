package basic.classroom.exception;

import lombok.Getter;

@Getter
public class UpdateMemberException extends RuntimeException {
    private ErrorCode errorCode;

    public UpdateMemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
