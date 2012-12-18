package org.swas.web.dto.form;


import org.swas.domain.User;
import org.swas.web.validator.FieldMatch;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 01.12.11
 */
@FieldMatch(original = "password", compared = "passwordC", message = "{validator.constraints.PasswordsNotEquals.message}")
public class SignUpForm extends ReCaptchaAwareForm {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
    @NotBlank
    @Size(min = 6, max = 255)
    private String passwordC;
    @Size(max = 255)
    private String name;
    private Long placeId;
    private String timezone;
    private String fromUrl;


    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordC() {
        return passwordC;
    }

    public void setPasswordC(String passwordC) {
        this.passwordC = passwordC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public User toUser() {
        User u = new User();
        u.setEmail(this.email);
        u.setLogin(this.email.replace("@", "'at'"));
        u.setPsw(this.password);
        u.setName(this.email.substring(0, this.email.indexOf('@')));
        if (StringUtils.isNotBlank(this.name))
            u.setName(this.name);
        u.setTimezone(this.timezone);
        return u;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

}
