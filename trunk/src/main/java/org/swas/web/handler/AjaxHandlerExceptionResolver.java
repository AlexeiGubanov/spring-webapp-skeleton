package org.swas.web.handler;

import org.swas.web.helper.UrlHelper;
import org.swas.web.util.AntUrlPatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 07.12.11
 */
@Component
public class AjaxHandlerExceptionResolver extends AbstractUrlMatchHandlerExceptionResolver {

    private static final String[] configUrls = UrlHelper.AJAX_URLS;

    private static final Logger log = LoggerFactory.getLogger(AjaxHandlerExceptionResolver.class);

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public AjaxHandlerExceptionResolver() {
        this.setMatcher(new AntUrlPatternMatcher(configUrls));
    }

    @Override
    protected ModelAndView doResolveMatchedException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.warn("Error during process request", ex);
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ModelAndView();
    }
}
