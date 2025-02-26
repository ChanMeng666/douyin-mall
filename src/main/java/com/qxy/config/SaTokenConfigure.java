package com.qxy.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.qxy.Interceptor.CustomInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Gloss66
 * @version 1.0
 * @description: Sa-Token 配置类
 */

@Configuration
@EnableWebMvc
@Slf4j
public class SaTokenConfigure implements WebMvcConfigurer {
    @Resource
    private CustomInterceptor customInterceptor;
    //配置token有效期
    private long tokenTimeout = 24 * 60 * 60;
//    // Sa-Token 参数配置，参考文档：https://sa-token.cc
//    @Autowired
//    public void configSaToken(SaTokenConfig config) {
//        config.setTokenName("satoken");             // token 名称（同时也是 cookie 名称）
//        config.setTimeout(tokenTimeout);       // token 有效期（单位：秒），默认30天，-1代表永不过期
//        config.setActiveTimeout(1800);              // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
//        config.setIsConcurrent(false);               // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
//        config.setIsShare(false);                    // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
//        config.setTokenStyle("uuid");               // token 风格
//        config.setIsLog(true);                     // 是否输出操作日志
//        config.setIsColorLog(true);              //彩色日志
//        config.setAutoRenew(false);               //是否自动续签activeToken
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有路径
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOrigins("*")
                // 允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许发送 Cookie
                .allowCredentials(false)
                // 预检请求的缓存时间
                .maxAge(3600);
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor)
                .addPathPatterns("/**");
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录拦截
            SaRouter
                    .match("/**")    // 拦截的 path 列表，可以写多个 */
                    .notMatch("/user/doLogin/**")        // 排除掉的 path 列表，可以写多个
                    .notMatch("/user/isLogin/**").notMatch("/user/USER/SignUp/**")
                    .notMatch("/user/LoginByCode/**").notMatch("/user/SendPhoneCode/**")
                    .notMatch("/user/SendEmailCode/**").notMatch("/user/getInfo/**")
                    .notMatch("/user/checkCode/**")
                    .check(r ->{
//                        StpUtil.updateLastActiveToNow();
                        StpUtil.checkLogin();
//                        log.info("更新前，当前登录有效时间："  + StpUtil.getTokenTimeout());
//                        StpUtil.renewTimeout(tokenTimeout); // 更新过期时间
//                        log.info("更新后，当前登录有效时间："  + StpUtil.getTokenTimeout());
                    } );        // 要执行的校验动作，可以写完整的 lambda 表达式
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
        ;
    }
}

