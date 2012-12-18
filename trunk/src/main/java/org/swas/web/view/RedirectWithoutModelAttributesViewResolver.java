package org.swas.web.view;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class RedirectWithoutModelAttributesViewResolver implements ViewResolver, Ordered {

    // Have a highest priority by default
    private int order = Integer.MIN_VALUE;

    // Uses this prefix to avoid interference with the default behaviour
    public static final String REDIRECT_URL_PREFIX = "redirectWithoutModel:";

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl, true, true, false);
        }
        return null;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

