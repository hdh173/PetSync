package com.coursework.petsync.project.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HDH
 * @version 1.0
 */
/**
 * 通用响应结构
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(name = "PetsyncResponse", description = "统一返回结构")
public class PetsyncResponse<T> {

    @Schema(description = "业务状态码", example = "200")
    private int code;

    @Schema(description = "提示信息", example = "success")
    private String msg;

    @Schema(description = "响应数据，可能为 null")
    private T data;

    public static <T> PetsyncResponse<T> success(T data) {
        return new PetsyncResponse<>(200, "success", data);
    }

    public static <T> PetsyncResponse<T> status(int code, String message,  T data) {
        return new PetsyncResponse<>(code, message, data);
    }

    public static <T> PetsyncResponse<T> failure(int code, String msg) {
        return new PetsyncResponse<>(code, msg, null);
    }

    public static <T> PetsyncResponse<T> error(int code, String msg) {
        return new PetsyncResponse<>(code, msg, null);
    }
}