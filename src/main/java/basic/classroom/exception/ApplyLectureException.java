package basic.classroom.exception;

import lombok.Getter;

@Getter
public class ApplyLectureException extends RuntimeException {
    private ErrorCode errorCode;

    public ApplyLectureException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
