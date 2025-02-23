package com.qxy.common.exception.exceptionhandler;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSON;
import com.qxy.common.exception.BusinessException;
import com.qxy.common.response.Response;
import com.qxy.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理 BusinessException 异常
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400 状态码
    public Response<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", System.currentTimeMillis());
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        response.put("error", "Bad Request");
//        response.put("message", ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        String requestURI = request.getRequestURI();
        ResponseCode responseCode = ex.getResponseCode();
        Object msg = ex.getMsg();
        if(responseCode==null) {
            responseCode = ResponseCode.UN_ERROR;
        }
            log.error("请求地址'{}',发生业务异常: {}", requestURI, responseCode.getInfo()+", "+JSON.toJSON(msg) , ex);
            return Response.fail(responseCode,msg);
    }

    /**
     * Satoken框架封装了异常类，此处直接拦截并处理相关异常
     */
    // 拦截：未登录异常
    @ResponseBody
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 返回 401 状态码
    public Response<?> handleNotLoginException(@NotNull NotLoginException nle) {
        // 打印堆栈，以供调试
        nle.printStackTrace();
        // 不同异常返回不同状态码
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供Token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "未提供有效的Token";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "登录信息已过期，请重新登录";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "您的账户已在另一台设备上登录，如非本人操作，请立即修改密码";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "已被系统强制下线";
        } else {
            message = "当前会话未登录";
        }
        return new Response<>("401",message,null);
    }

    // 拦截：缺少权限异常
    @ResponseBody
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handleNotPermissionException(NotPermissionException e) {
        e.printStackTrace();
        return SaResult.error("缺少权限：" + e.getPermission());
    }

    // 拦截：缺少角色异常
    @ResponseBody
    @ExceptionHandler(NotRoleException.class)
    public SaResult handleNotRoleException(NotRoleException e) {
        e.printStackTrace();
        return SaResult.error("缺少角色：" + e.getRole());
    }

    // 拦截：二级认证校验失败异常
    @ExceptionHandler(NotSafeException.class)
    public SaResult handleNotSafeException(NotSafeException e) {
        e.printStackTrace();
        return SaResult.error("二级认证校验失败：" + e.getService());
    }

    // 拦截：服务封禁异常
    @ExceptionHandler(DisableServiceException.class)
    public SaResult handleDisableServiceException(DisableServiceException e) {
        e.printStackTrace();
        return SaResult.error("当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封");
    }

}
