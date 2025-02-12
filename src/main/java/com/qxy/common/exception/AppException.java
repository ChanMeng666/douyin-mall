package com.qxy.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {


    private static final long serialVersionUID = 5317680961212299217L;

    private String code;
    private String info;

    // 关键修复：所有构造方法显式传递 message 到父类
    public AppException(String code) {
        super("");  // 初始化父类 message
        this.code = code;
        this.info = "";
    }

    public AppException(String code, Throwable cause) {
        super("", cause);  // 初始化父类 message
        this.code = code;
        this.info = "";
    }

    public AppException(String code, String info) {
        super(info);  // 核心修复：传递 info 到父类 message
        this.code = code;
        this.info = info;
    }

    public AppException(String code, String info, Throwable cause) {
        super(info, cause);  // 传递 info 到父类 message
        this.code = code;
        this.info = info;
    }

    @Override
    public String toString() {
        // 确保输出所有关键信息
        return String.format("AppException{code='%s', info='%s', message='%s'}",
                code, info, getMessage());
    }

}
