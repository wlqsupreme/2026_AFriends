package Afriends_v3.handler;

import Afriends_v3.entity.UserInfo_njj;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserKind枚举类型处理器
 * 处理数据库中存储的中文字符串与Java枚举之间的转换
 * 
 * 注意：此TypeHandler用于将数据库中的中文字符串（如"演员"、"真人"）
 * 转换为UserKind枚举类型，而不是使用枚举名称进行转换
 */
@MappedTypes(UserInfo_njj.UserKind.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class UserKindTypeHandler extends BaseTypeHandler<UserInfo_njj.UserKind> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserInfo_njj.UserKind parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getChineseName());
    }

    @Override
    public UserInfo_njj.UserKind getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convertToUserKind(value);
    }

    @Override
    public UserInfo_njj.UserKind getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return convertToUserKind(value);
    }

    @Override
    public UserInfo_njj.UserKind getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convertToUserKind(value);
    }

    /**
     * 将数据库中的字符串值转换为UserKind枚举
     */
    private UserInfo_njj.UserKind convertToUserKind(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        // 使用枚举中定义的fromChineseName方法
        UserInfo_njj.UserKind result = UserInfo_njj.UserKind.fromChineseName(value.trim());
        
        // 如果找不到匹配的枚举值，返回null或默认值
        if (result == null) {
            System.out.println("UserKindTypeHandler: 无法识别的UserKind值: " + value);
            return null; // 或者返回一个默认值，比如 UserInfo_njj.UserKind.REAL
        }
        
        return result;
    }
}