package org.swas.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
public class HttpRequestValueExtractor implements ValueExtractor {

    private HttpServletRequest request;

    public HttpRequestValueExtractor(HttpServletRequest request) {
        if (request == null)
            throw new IllegalStateException("Param 'request' can't be null");
        this.request = request;
    }

    public Object extractValue(String name) {
        return request.getParameter(name);
    }


}
