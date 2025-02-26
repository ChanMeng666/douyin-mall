package com.qxy.Interceptor;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 自定义拦截器
 */

@Slf4j
@Component
public class CustomInterceptor implements HandlerInterceptor {
    @Value("${sa-token.token-name}")
    private String tokenName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        long tokenActivityTimeout = StpUtil.getTokenActiveTimeout();
        log.info("tokenActivityTimeout:{}", tokenActivityTimeout);
        long tokenTimeout = StpUtil.getTokenTimeout();
        if (tokenActivityTimeout > 0){
            response.setHeader(tokenName, StpUtil.getTokenValue());
            response.setHeader("Access-Control-Expose-Headers", tokenName);
        }
        if (tokenActivityTimeout < 0 && tokenTimeout > 0){
            // 首先要让token活跃
            StpUtil.updateLastActiveToNow();
            String loginId = (String) StpUtil.getLoginId();
            String device = StpUtil.getLoginDevice();
            // 先退出，否则之前的token还能用
            StpUtil.logout(loginId);
            // 重新设置token，这里仅仅是为了安全，否则始终token是一个值
            StpUtil.login(loginId,new SaLoginModel()
                    .setDevice(device)
                    .setTimeout(tokenTimeout));
            // 请求头修改token的值，否则在第二个拦截器会报错，因为老的token已经失效了
            request.setAttribute(tokenName, StpUtil.getTokenValue());
            // 响应头设置值
            response.setHeader(tokenName, StpUtil.getTokenValue());
            response.setHeader("Access-Control-Expose-Headers", tokenName);
        }
        return true;
    }
}
