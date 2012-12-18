package org.swas.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Simple UrlPatternMatcher implementation, based on {@link AntPathMatcher} matcher.
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class AntUrlPatternMatcher implements UrlPatternMatcher {
    private static final Logger log = LoggerFactory.getLogger(AntUrlPatternMatcher.class);

    private String[] patterns;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private HashMap<String, Boolean> urlCache = new HashMap<String, Boolean>();

    public AntUrlPatternMatcher() {
    }

    public AntUrlPatternMatcher(String[] patterns) {
        this.patterns = patterns;
    }

    /**
     * @param alwaysUseFullPath - flag to use alwaysUseFullPath
     * @see UrlPathHelper#setAlwaysUseFullPath(boolean)
     */
    public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
        this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
    }

    /**
     * @param urlDecode - flag to use url decoding
     * @see UrlPathHelper#setUrlDecode(boolean)
     */
    public void setUrlDecode(boolean urlDecode) {
        this.urlPathHelper.setUrlDecode(urlDecode);
    }

    /**
     * Set external urlPathHelper instance.
     * By default class use new instance;
     *
     * @param urlPathHelper - external urlPathHelper
     */
    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    /**
     * Set list of excluded patterns in ant-path style.
     *
     * @param patterns - list of excluded patterns
     * @see AntPathMatcher
     */
    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    /**
     * @return current UrlPathHelper
     */
    public UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
    }

    public boolean isMatch(String path) {
        Boolean result = urlCache.get(path);
        if (result != null)
            return result;
        result = false;
        for (String p : patterns) {
            if (pathMatcher.match(p, path)) {
                result = true;
                break;
            }
        }
        urlCache.put(path, result);
        return result;
    }

    public boolean isMatch(HttpServletRequest request) {
        return isMatch(getUrlPathHelper().getPathWithinApplication(request));
    }

}
