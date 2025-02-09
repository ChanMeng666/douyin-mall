package com.qxy.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dawang
 * @Description: TODO
 * @Date: 2025/1/15 23:30
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T>{
    private String code;
    private String info;
    private T data;

    public static Response success() {
        return new Response(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), null);
    }

    /**
     * 一般结果响应，可自定义响应码和信息
     *
     * @param code 响应码
     * @param info 响应信息
     * @return Response类
     */
    public void result(String code, String info, T data){
        this.code=code;
        this.info=info;
        this.data=data;
    }
}
