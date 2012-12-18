package org.swas.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adapter that intercept {@link #preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, Object)} method
 * and don't allow next processing if current url is excluded.
 * Used for add 'excluded url' features to existing interceptors.
 *
 * @author GubanovAS
 *         Date: 24.08.11
 */
public class DelegatingExcludedUrlsInterceptor implements HandlerInterceptor {

    private ExcludedUrlsAware urlHelper;

    private HandlerInterceptor delegate;

    public void setUrlHelper(ExcludedUrlsAware urlHelper) {
        this.urlHelper = urlHelper;
    }

    public void setDelegate(HandlerInterceptor delegate) {
        this.delegate = delegate;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return urlHelper.isExcluded(request) || delegate.preHandle(request, response, handler);
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!urlHelper.isExcluded(request))
            delegate.postHandle(request, response, handler, modelAndView);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!urlHelper.isExcluded(request))
            delegate.afterCompletion(request, response, handler, ex);
    }


}
