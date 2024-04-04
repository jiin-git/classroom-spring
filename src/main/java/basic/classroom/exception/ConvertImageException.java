package basic.classroom.exception;

import lombok.Getter;

@Getter
public class ConvertImageException extends RuntimeException {
    private ErrorCode errorCode;

    public ConvertImageException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
