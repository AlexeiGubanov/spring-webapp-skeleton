package org.swas.web.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.swas.web.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (WebUtils.getSessionAttribute(request, SessionAttribute.CONTEXT) == null) {
            SessionAttribute.CONTEXT_VAL = request.getContextPath();
            WebUtils.setSessionAttribute(request, SessionAttribute.CONTEXT, SessionAttribute.CONTEXT_VAL);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
