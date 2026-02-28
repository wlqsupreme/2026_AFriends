package Interview_v3.common;

import lombok.Data;

/**
 * 通用返回结果类
 * 所有接口统一返回该格式
 */
@Data
public class Result<T> {
    // 响应码：200成功，500失败，400参数错误，401未登录，403无权限
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;

    // 成功返回（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功返回（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败返回
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    // 自定义返回
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}