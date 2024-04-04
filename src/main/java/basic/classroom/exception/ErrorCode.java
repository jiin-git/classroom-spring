package basic.classroom.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    AUTHENTICATION_FAILED(400, "로그인에 실패했습니다. 아이디나 비밀번호를 다시 확인해주세요."),
    INVALID_MEMBER_STATUS(400, "유효하지 않는 회원 상태입니다."),

    FAILED_CHANGE_PASSWORD(400, "비밀번호가 일치하지 않습니다. 다시 확인해주세요."),
    FAILED_CONVERT_PROFILE_IMAGE(400, "요청한 이미지 파일을 인코딩하는데 실패했습니다."),
    FAILED_STORE_IMAGE(400, "이미지 저장에 실패했습니다. 이미지 파일 형식과 용량을 다시 확인해주세요."),
    FAILED_CHANGE_LECTURE_PERSONNEL(400, "신청한 정원보다 적은 수로 변경할 수 없습니다."),
    FAILED_CHANGE_LECTURE_STATUS_READY(400, "신청한 학생이 있어 강의를 준비 상태로 변경할 수 없습니다."),
    FAILED_CHANGE_LECTURE_STATUS_OPEN(400, "정원이 초과되어 강의를 오픈 상태로 변경할 수 없습니다."),
    FAILED_CHANGE_LECTURE_STATUS_FULL(400, "정원이 미달되어 강의를 닫을 수 없습니다."),
    FAILED_APPLY_LECTURE(400, "강의를 신청할 수 없습니다."),
    FAILED_FIND_LECTURE_BY_SEARCH_CONDITION(400, "조건과 검색어를 함께 사용해야합니다."),

    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    LECTURE_NOT_FOUND(404, "강의를 찾을 수 없습니다."),
    LECTURE_ACCESS_DENIED(404, "강의를 찾을 수 없습니다."),

    DUPLICATED_LOGIN_ID(409, "중복된 로그인 아이디입니다.");

    private final int status;
    private final String message;
}
