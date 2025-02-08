// com.qxy.common.handler.ProductStatusTypeHandler.java
package com.qxy.common.handler;

import com.qxy.model.enums.ProductStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author water
 * @description: 自定义 ProductStatus 枚举类型处理器
 * @date 2025/2/5 23:15
 * @version 1.0
 */
public class ProductStatusTypeHandler extends BaseTypeHandler<ProductStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ProductStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name().toLowerCase()); // 将枚举值转为小写存储
    }

    @Override
    public ProductStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : ProductStatus.valueOf(value.toUpperCase()); // 将数据库值转为大写后映射
    }

    @Override
    public ProductStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : ProductStatus.valueOf(value.toUpperCase());
    }

    @Override
    public ProductStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : ProductStatus.valueOf(value.toUpperCase());
    }
}