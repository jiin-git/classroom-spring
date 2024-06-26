package basic.classroom.exception;

import lombok.Getter;

@Getter
public class CreateDuplicatedMemberException extends RuntimeException {
    private ErrorCode errorCode;

    public CreateDuplicatedMemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
