package org.swas.web.interceptor;

import org.swas.web.util.UrlPatternMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Adapter that checks if url is excluded using {@link UrlPatternMatcher}.
 *
 * @author GubanovAS
 *         Date: 18.08.11
 */
public class ExcludedUrlsHelper implements ExcludedUrlsAware {

    private UrlPatternMatcher excludedUrls;

    public UrlPatternMatcher getExcludedUrls() {
        return excludedUrls;
    }

    public void setExcludedUrls(UrlPatternMatcher excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    public boolean isExcluded(String path) {
        return excludedUrls.isMatch(path);
    }

    public boolean isExcluded(HttpServletRequest request) {
        return excludedUrls.isMatch(request);
    }
}
