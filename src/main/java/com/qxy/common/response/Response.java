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

    /**
     * 默认成功响应
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> success() {
        return new Response<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), null);
    }

    /**
     * 无data的成功响应
     * @param code
     * @param msg
     * @return Response<T>
     */
    public static <T> Response<T> success(String code, String msg){
        return new Response<T>(code,msg,null);
    }

    /**
     * 带data的成功响应
     * @param code
     * @param msg
     * @param data
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> success(String code, String msg, T data){
        return new Response<T>(code,msg,data);
    }

    /**
     * 指定响应码的失败响应
     * @param responseCode
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> success(ResponseCode responseCode,T data){
        return new Response<T>(responseCode.getCode(), responseCode.getInfo(),data);
    }

    /**
     * 默认失败响应
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> fail() {
        return new Response<T>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo(), null);
    }

    /**
     * 无data的失败响应
     * @param code
     * @param msg
     * @return Response<T>
     */
    public static <T> Response<T> fail(String code, String msg){
        return new Response<T>(code,msg,null);
    }

    /**
     * 带data的失败响应
     * @param code
     * @param msg
     * @param data
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> fail(String code, String msg, T data){
        return new Response<T>(code,msg,data);
    }

    /**
     * 指定响应码的失败响应
     * @param responseCode
     * @return Response<T>
     * @param <T>
     */
    public static <T> Response<T> fail(ResponseCode responseCode,T data){
        if(responseCode==null) responseCode = ResponseCode.UN_ERROR;
        return new Response<T>(responseCode.getCode(), responseCode.getInfo(),data);
    }
}
