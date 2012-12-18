package org.swas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import org.swas.dao.util.ListParams;
import org.swas.dao.util.PagedList;
import org.swas.domain.User;
import org.swas.domain.UserStatus;
import org.swas.service.ActivationUrlGenerator;
import org.swas.service.Result;
import org.swas.service.UserService;
import org.swas.util.MessagesHelper;
import org.swas.util.SystemParameters;
import org.swas.web.AttemptsSessionStorage;
import org.swas.web.SessionAttribute;
import org.swas.web.dto.Response;
import org.swas.web.dto.UserDto;
import org.swas.web.dto.form.ReCaptchaAwareForm;
import org.swas.web.dto.form.RestorePasswordForm;
import org.swas.web.dto.form.SignInForm;
import org.swas.web.dto.form.SignUpForm;
import org.swas.web.dto.params.UserListRequestParams;
import org.swas.web.helper.ReCaptchaHelper;
import org.swas.web.helper.UrlHelper;
import org.swas.web.helper.ViewHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 01.12.11
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private ReCaptchaHelper captchaHelper;

    @Autowired
    private MessagesHelper mh;

    @Autowired
    private ViewHelper vh;


    public String generateCaptcha(Locale locale) {
        if (!"1".equals(SystemParameters.getParam("testMode.recaptcha"))) {
            return captchaHelper.getCaptchaHtml(locale.getLanguage());
        }
        return "";
    }

    private Response checkBots(HttpServletRequest request, Locale locale, ReCaptchaAwareForm form) {
        // логируем запрос
        AttemptsSessionStorage as = AttemptsSessionStorage.forSession(request.getSession());
        // смотрим валидацию
        if (!as.isGood(request.getRemoteAddr())) {
            Properties p = new Properties();
            p.put("needRecaptcha", "true");
            if (form.haveRecaptcha()) {
                // валидируем
                if (!"1".equals(SystemParameters.getParam("testMode.recaptcha"))) {
                    if (!captchaHelper.checkCaptcha(request.getRemoteAddr(), form.getRecaptchaChallenge(), form.getRecaptchaResponse())) {
                        return Response.error(p, mh.getMessage("antiBots.recaptchaInvalid", locale));
                    }
                }
            } else {
                return Response.error(p, mh.getMessage("antiBots.needRecaptcha", locale));

            }
        }
        as.log(request.getRemoteAddr());
        return null;
    }

    @RequestMapping(value = UrlHelper.URL_SIGNUP, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Response signUp(@RequestBody SignUpForm form, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {


        Errors errors = new BeanPropertyBindingResult(form, "signUpForm");
        validator.validate(form, errors);

        if (errors.hasErrors()) {
            return Response.error(errors, mh.getMessageSource(), locale);
        }

        if (userService.isEmailExists(form.getEmail())) {
            errors.rejectValue("email", "validator.constraints.EmailExists.message");
            return Response.error(errors, mh.getMessageSource(), locale);

        }

        Response r1 = checkBots(request, locale, form);
        if (r1 != null)
            return r1;

        User user = form.toUser();
        Result r = userService.register(user, locale, new ActivationUrlGeneratorImpl(request));
        if (!r.isOk()) {
            return Response.error(mh.getMessage("signUp.commonError", locale));
        }
        user = (User) r.getValue();


        return Response.success(mh.getMessage("signUp.success", new Object[]{user.getEmail()}, locale));

    }


    @RequestMapping(value = UrlHelper.URL_ACTIVATE, method = {RequestMethod.GET})
    public String activate(@RequestParam String ac, HttpServletRequest request) {
        Result r = userService.activate(ac);
        if (!r.isOk()) {
            switch (r.getCode()) {
                case UserService.USER_ALREADY_ACTIVATED:
                    return vh.getMessageView("activation.exists", request);
                case UserService.USER_NOT_FOUND:
                    return vh.getMessageView("activation.notFound", request);
            }
        }
        return vh.getMessageView("activation.ok", request);
    }

    private Response signInInternal(SignInForm form, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
        Errors errors = new BeanPropertyBindingResult(form, "signInForm");
        validator.validate(form, errors);

        if (errors.hasErrors()) {
            return Response.error(errors, mh.getMessageSource(), locale);
        }

        Response r1 = checkBots(request, locale, form);
        if (r1 != null)
            return r1;

        Result r = userService.authorize(form.getLogin(), form.getPassword());
        if (r.isOk()) {
            User u = (User) r.getValue();
            WebUtils.setSessionAttribute(request, SessionAttribute.PROFILE, u);


            String redirectTo = form.getRedirectTo();
            if (redirectTo == null) {
                if (form.getFromUrl() != null)
                    redirectTo = form.getFromUrl();
                else
                    redirectTo = "";
            }
            return Response.success(redirectTo);
        }
        switch (r.getCode()) {
            case UserService.USER_NOT_FOUND:
                errors.rejectValue("login", "signIn.userNotFound");
                break;
            case UserService.USER_INCORRECT_PASSWORD:
                errors.rejectValue("password", "signIn.incorrectPassword");
                break;
            case UserService.USER_NOT_ACTIVATED:
                errors.reject("signIn.userNotActivated");
                break;
        }
        return Response.error(errors, mh.getMessageSource(), locale);
    }


    @RequestMapping(value = UrlHelper.URL_SIGNIN, method = {RequestMethod.GET, RequestMethod.POST},
            headers = "content-type=application/json")
    @ResponseBody
    public Response signIn(@RequestBody SignInForm form, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
        return signInInternal(form, request, response, locale);

    }

    @RequestMapping(value = UrlHelper.URL_SIGNIN, method = {RequestMethod.GET})
    @ResponseBody
    public Response signIn2(SignInForm form, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
        return signInInternal(form, request, response, locale);
    }

    @RequestMapping(value = UrlHelper.URL_SIGNOUT, method = {RequestMethod.GET})
    public String signOut(HttpServletRequest request) throws Exception {
        WebUtils.setSessionAttribute(request, SessionAttribute.PROFILE, null);
        request.getSession().invalidate();
        return "redirectWithoutModel:";
    }


    @RequestMapping(value = UrlHelper.URL_PASSWORD_RESTORE, method = RequestMethod.POST)
    @ResponseBody
    public Response passwordRestore(@RequestBody RestorePasswordForm form, Locale locale) {
        Errors errors = new BeanPropertyBindingResult(form, "restorePasswordForm");
        validator.validate(form, errors);

        if (errors.hasErrors()) {
            return Response.error(errors, mh.getMessageSource(), locale);
        }
        Result r = userService.passwordRestore(form.getIdentity(), locale);
        if (!r.isOk()) {
            switch (r.getCode()) {
                case UserService.USER_NOT_FOUND:
                    return Response.error(mh.getMessage("passwordRestore.notFound", locale));
                default:
                    return Response.error(mh.getMessage("passwordRestore.generalError", locale));
            }
        }
        return Response.success(mh.getMessage("passwordRestore.ok", locale));
    }

    @RequestMapping(value = UrlHelper.URL_USER_VIEW, method = RequestMethod.GET)
    public String usersView() {
        return ViewHelper.VIEW_ADMIN_USER;
    }


    @RequestMapping(value = UrlHelper.URL_USER_LIST, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Response usersList(UserListRequestParams params) {
        params.fillUpParamsDefault();
        ListParams lp = params.asListParams();
        lp.setUsePagination(true);
        PagedList<User> list = (PagedList<User>) userService.findAll(lp);
        List<UserDto> l = new ArrayList<UserDto>();
        for (User c : list) {
            l.add(new UserDto(c));
        }
        Response r = Response.success(l);
        r.setTotal(list.getTotalCount());
        return r;
    }

    @RequestMapping(value = UrlHelper.URL_USER_VIEW_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public Response usersUpdate(@RequestBody UserDto... data) {

        for (UserDto ut : data) {
            User us = userService.get(ut.getId());
            us.setStatus(UserStatus.forCode(ut.getStatus()));
            Result r = userService.update(us);
            if (r.isOk() == false) {
                return Response.error(r.getMsg());
            }
        }

        return Response.success("");
    }

    private class ActivationUrlGeneratorImpl implements ActivationUrlGenerator {
        private HttpServletRequest request;

        private ActivationUrlGeneratorImpl(HttpServletRequest request) {
            this.request = request;
        }

        public String generateUrl(User user) {
            String template = SystemParameters.getParam("accountActivation.url");
            String serverName = request.getServerName();
            if (request.getServerPort() != 80) {
                serverName += ":" + request.getServerPort();
            }
            template = template.replaceAll("\\{server\\}", serverName);
            template = template.replaceAll("\\{context\\}", request.getContextPath());
            template = template.replaceAll("\\{activateUrl\\}", UrlHelper.URL_ACTIVATE);
            template = template.replaceAll("\\{activationCode\\}", user.getActivationCode());
            return template;
        }
    }
}
