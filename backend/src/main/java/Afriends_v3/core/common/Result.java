package Afriends_v3.core.common;

import lombok.Data;

/**
 * 统一响应实体类
 */
@Data
public class Result {
    // 响应状态码：200成功，非200失败
    private int code;
    // 响应消息
    private String message;
    // 响应数据（成功时返回）
    private Object data;

    // 私有构造方法，禁止直接实例化
    private Result() {}

    /**
     * 成功响应（带数据）
     */
    public static Result success(Object data, String message) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（默认消息）
     */
    public static Result success(String message) {
        return success(null, message);
    }

    /**
     * 失败响应（带错误消息）
     */
    public static Result fail(String message) {
        Result result = new Result();
        result.setCode(500); // 通用错误码，可根据业务细化
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 失败响应（带错误码和消息）
     */
    public static Result fail(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
