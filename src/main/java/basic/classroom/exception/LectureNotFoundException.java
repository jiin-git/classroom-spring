package basic.classroom.exception;

import lombok.Getter;

@Getter
public class LectureNotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    public LectureNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
