package com.hongkai.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hongkai.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import util.Constants;

/**
 * @author huiliu
 * @date 17/9/5
 */
@Configuration
@Slf4j
public class LogInterceptor extends WebMvcConfigurerAdapter {
    @Autowired
    LoginInterceptor loginInterceptor;
    /**
     * 生成随机字符串
     */
    private static UniformRandomProvider RNG = RandomSource.create(RandomSource.MT);
    private static RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder()
        .withinRange('a', 'z')
        .usingRandom(RNG::nextInt)
        .build();
    private static Integer TRACE_ID_LENGTH = 20;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
                MDC.put(Constants.TRACE_ID, GENERATOR.generate(TRACE_ID_LENGTH));
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                                   ModelAndView modelAndView) throws Exception {

            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        Exception ex)
                throws Exception {
                MDC.remove(Constants.TRACE_ID);
            }
        });

        registry.addInterceptor(loginInterceptor);
    }
}
