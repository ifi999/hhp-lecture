package com.hhp.lecture.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int httpStatus;
    private T data;

    public ApiResponse(final int httpStatus, final T data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public static <T> ApiResponse<T> isOk(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data);
    }

}
