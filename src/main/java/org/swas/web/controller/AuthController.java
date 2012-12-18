package org.swas.web.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;
import org.swas.dao.UserDao;
import org.swas.domain.User;
import org.swas.domain.UserStatus;
import org.swas.web.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
@Controller
public class AuthController {

    private RestTemplate restTemplate;

    @Autowired
    private UserDao userDao;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
        restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(messageConverters);
        converters.add(new OAuthResponseConverter());
        restTemplate.setMessageConverters(converters);
    }

    @Transactional
    @RequestMapping(value = "/auth", method = {RequestMethod.POST, RequestMethod.GET})
    public String processAuth(@RequestParam String token, @RequestParam String urlFrom, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OAuthResponse r = restTemplate.getForObject("http://ulogin.ru/token.php?token={token}&host={host}", OAuthResponse.class, token, request.getServerName());
        if (r.getUid() == null)
            return "error";
        //вероятнее всего придется делать callback для ulogin, который передаст сюда токен и действие

        // смотрим есть ли такой юзер
        String login = r.getNetwork() + "_" + r.getUid();
        User u = userDao.findByLogin(login);
        if (u == null) {
            u = new User();
            u.setLogin(login);
            u.setEmail(r.getEmail());
            u.setStatus(UserStatus.ACTIVE);
            u.setName(r.getFirst_name() + " " + r.getLast_name());
            u.setPsw(RandomStringUtils.random(20, "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890"));
            userDao.create(u);
        }
        WebUtils.setSessionAttribute(request, SessionAttribute.PROFILE, u);
        return "redirect:" + urlFrom;
    }

    public static class OAuthResponseConverter extends MappingJacksonHttpMessageConverter {

        @Override
        public boolean canRead(Class<?> clazz, MediaType mediaType) {
            if (clazz.equals(OAuthResponse.class))
                return true;
            return super.canRead(clazz, mediaType);
        }
    }

    public static class OAuthResponse {
        private String network;
        private String identity;
        private String uid;
        private String first_name;
        private String last_name;
        private String photo;
        private String manual;
        private String email;
        private String city;
        private String country;
        private String access_token;
        private String profile;

        private String verified_email;

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getManual() {
            return manual;
        }

        public void setManual(String manual) {
            this.manual = manual;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getVerified_email() {
            return verified_email;
        }

        public void setVerified_email(String verified_email) {
            this.verified_email = verified_email;
        }
    }

}
