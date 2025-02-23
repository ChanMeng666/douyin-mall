package com.qxy.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Gloss66
 * @version 1.0
 * @description: TODO
 */

/**
 * Sa-Token 配置类
 */
@Configuration
@EnableWebMvc
public class SaTokenConfigure implements WebMvcConfigurer {
    // Sa-Token 参数配置，参考文档：https://sa-token.cc
    @Autowired
    public void configSaToken(SaTokenConfig config) {
        config.setTokenName("satoken");             // token 名称（同时也是 cookie 名称）
        config.setTimeout(24 * 60 * 60);       // token 有效期（单位：秒），默认30天，-1代表永不过期
        config.setActiveTimeout(1800);              // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
        config.setIsConcurrent(false);               // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
        config.setIsShare(false);                    // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
        config.setTokenStyle("uuid");               // token 风格
        config.setIsLog(true);                     // 是否输出操作日志
        config.setIsColorLog(true);              //彩色日志
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录拦截
            SaRouter
                    .match("/**")    // 拦截的 path 列表，可以写多个 */
//                    .notMatch("/user/doLogin/**")        // 排除掉的 path 列表，可以写多个
//                    .notMatch("/user/isLogin/**").notMatch("/user/SignUp/**")
//                    .notMatch("/user/LoginByCode/**").notMatch("/user/SendCode/**")
                    .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式
            // 权限拦截
            SaRouter
                    .match("/api/product/create/**")    // 拦截的 path 列表，可以写多个 */
//                    .match("/user/getPermission")    // 拦截的 path 列表，可以写多个 */
                    .check(r -> StpUtil.checkPermission("add_product"));        // 要执行的校验动作，可以写完整的 lambda 表达式
//            // 身份拦截
//            SaRouter
//                    .match("/role/**")    // 拦截的 path 列表，可以写多个 */
//                    .check(r -> StpUtil.checkRole("user"));        // 要执行的校验动作，可以写完整的 lambda 表达式

        })).addPathPatterns("/**")
           .excludePathPatterns("/user/doLogin")
           .excludePathPatterns("/user/USER/SignUp")
           .excludePathPatterns("/user/LoginByCode")
           .excludePathPatterns("/user/SendPhoneCode")
           .excludePathPatterns("/user/SendEmailCode")
           .excludePathPatterns("/user/isLogin")
           .excludePathPatterns("/user/getInfo")
           .excludePathPatterns("/user/checkCode")
        ;
    }
}

