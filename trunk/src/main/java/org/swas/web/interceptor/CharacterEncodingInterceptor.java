package org.swas.web.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class CharacterEncodingInterceptor extends HandlerInterceptorAdapter {

    private String encoding = "UTF-8";

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding(encoding);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        response.setCharacterEncoding(encoding);
    }
}
