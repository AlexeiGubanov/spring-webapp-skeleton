package org.swas.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.swas.web.helper.UrlHelper;
import org.swas.web.helper.ViewHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@Controller
public class CommonController {

    private String timezoneCached;

    @RequestMapping(value = UrlHelper.URL_ADMIN_VIEW, method = RequestMethod.GET)
    public String adminView(HttpServletRequest request) {
        return ViewHelper.VIEW_ADMIN;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String rootView(HttpServletRequest request, Map<String, Object> model) {
        request.setAttribute("index", true);
        return "index";
    }

    @RequestMapping(value = UrlHelper.URL_ACCESS_DENIED, method = RequestMethod.GET)
    public String accessDeniedView(@RequestParam(required = false) String redirectTo, HttpServletRequest request) {
        if (redirectTo != null)
            request.setAttribute("redirectTo", redirectTo);
        return ViewHelper.VIEW_ACCESS_DENIED;
    }


}
