package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_SAME_SMALL_DISTANCE(HttpStatus.BAD_REQUEST, "새로운 구간의 길이는 기존 구간보다 크거나 같을 수 없습니다."),
    ALREADY_ENROLL_UP_AND_DOWN_STATION(HttpStatus.BAD_REQUEST, "이미 노선에 상행역과 하행역이 모두 등록되어 있습니다."),
    NOT_ENROLL_UP_AND_DOWN_STATION(HttpStatus.BAD_REQUEST, "새로운 구간의 상행역과 하행역이 노선에 포함되어 있지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}