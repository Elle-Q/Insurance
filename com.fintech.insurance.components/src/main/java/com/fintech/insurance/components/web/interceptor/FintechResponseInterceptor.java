package com.fintech.insurance.components.web.interceptor;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.MicroJsonMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器拦截器基类
 */
public class FintechResponseInterceptor extends HandlerInterceptorAdapter {

    protected MicroJsonMessageConverter jsonMessageConverter = null;

    public FintechResponseInterceptor() {
        this.jsonMessageConverter = MicroJsonMessageConverter.converterInstance;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).getBean() instanceof BaseFintechController) {
            //判断controller方法是否有返回值
            Class<?> returnType = ((HandlerMethod)handler).getMethod().getReturnType();
            //如果是void返回类型则直接输出成功消息
            if (Void.TYPE.equals(returnType)) {
                this.jsonMessageConverter.write(FintechResponse.responseOk(), MediaType.APPLICATION_JSON_UTF8, new ServletServerHttpResponse(response));
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
