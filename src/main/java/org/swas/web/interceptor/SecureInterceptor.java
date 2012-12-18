package org.swas.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import org.swas.domain.User;
import org.swas.web.SessionAttribute;
import org.swas.web.helper.UrlHelper;
import org.swas.web.util.AntUrlPatternMatcher;
import org.swas.web.util.UrlPatternMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: GubanovAS
 * Date: 09.08.11
 * Time: 9:37
 */
public class SecureInterceptor extends HandlerInterceptorAdapter {

    private UrlPatternMatcher authRequiredMatcher = new AntUrlPatternMatcher(new String[]{"/admin/**"});

    private UrlPathHelper urlPathHelper = new UrlPathHelper();


    public void setAuthRequiredMatcher(UrlPatternMatcher authRequiredMatcher) {
        this.authRequiredMatcher = authRequiredMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User u = (User) WebUtils.getSessionAttribute(request, SessionAttribute.PROFILE);
        String path = urlPathHelper.getPathWithinApplication(request);


        if (u == null) {
            if (authRequiredMatcher.isMatch(path)) {
                if (path.startsWith("/"))
                    path = path.substring(1);
                request.setAttribute("redirectTo", path);
                response.sendRedirect(request.getContextPath() + UrlHelper.URL_ACCESS_DENIED + "?redirectTo=" + path);
                return false;
            }
        }

        return true;
    }
}
