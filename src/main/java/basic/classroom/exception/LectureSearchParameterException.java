package basic.classroom.exception;

import lombok.Getter;

@Getter
public class LectureSearchParameterException extends RuntimeException{
    private ErrorCode errorCode;

    public LectureSearchParameterException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
