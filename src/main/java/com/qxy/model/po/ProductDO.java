package com.qxy.model.po;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity // 标识这是一个实体类
@Table(name = "products") // 指定映射的表名
@Data // Lombok注解，自动生成getter/setter/toString等方法
public class ProductDO {

    @Id // 标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    @Column(name = "product_id") // 映射到表的product_id列
    private Integer productId;

    @Column(name = "name", nullable = false) // 映射到name列，非空
    private String name;

    @Column(name = "description") // 映射到description列
    private String description;

    @Column(name = "price", nullable = false) // 映射到price列，非空
    private BigDecimal price;

    @Column(name = "stock", nullable = false) // 映射到stock列，非空
    private Integer stock;

    @Column(name = "created_at", updatable = false) // 映射到created_at列，不可更新
    private Date createdAt;

    @Column(name = "updated_at") // 映射到updated_at列
    private Date updatedAt;

    @Enumerated(EnumType.STRING) // 将枚举类型映射为字符串
    @Column(name = "status", columnDefinition = "ENUM('active', 'inactive') DEFAULT 'active'") // 映射到status列
    private ProductStatus status;

    @Column(name = "image_url")
    private String imageUrl;

    // 定义枚举类型
    public enum ProductStatus {
        ACTIVE, INACTIVE
    }
}
