package org.swas.web.dto.form;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 15.02.12
 */
public abstract class ReCaptchaAwareForm {

    private String recaptchaChallenge;
    private String recaptchaResponse;

    public String getRecaptchaChallenge() {
        return recaptchaChallenge;
    }

    public void setRecaptchaChallenge(String recaptchaChallenge) {
        this.recaptchaChallenge = recaptchaChallenge;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
    }

    public boolean haveRecaptcha() {
        return StringUtils.isNotEmpty(recaptchaChallenge) && StringUtils.isNotEmpty(recaptchaResponse);
    }
}
