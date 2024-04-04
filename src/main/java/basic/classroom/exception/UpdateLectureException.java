package basic.classroom.exception;

import lombok.Getter;

@Getter
public class UpdateLectureException extends RuntimeException{
    private ErrorCode errorCode;

    public UpdateLectureException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
