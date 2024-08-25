package com.sparta.schedulemanagement.common.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String errorCode;
    private String errorMessage;;

    // 성공 응답 생성자
    public ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // 실패 응답 생성자
    public ApiResponse(boolean success, String errorCode, String errorMessage) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // 공통 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data);
    }

    // 공통 오류 응답 생성 메서드
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        return new ApiResponse<>(false, errorCode, errorMessage);
    }


}
