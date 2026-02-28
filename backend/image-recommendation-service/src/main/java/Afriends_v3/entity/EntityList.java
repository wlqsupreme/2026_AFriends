package Afriends_v3.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 通用实体列表基础类（解决继承报错）
 * 可根据实际需求扩展通用方法
 */
public class EntityList<T> implements Serializable {
    // 通用空构造器（必须）
    public EntityList() {}

    // 可选：通用列表操作方法（示例）
    protected void validateList(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("数据列表不能为空");
        }
    }

    // 可选：通用日志方法
    protected void logOperation(String message) {
        System.out.println("[EntityList] " + message);
    }
}