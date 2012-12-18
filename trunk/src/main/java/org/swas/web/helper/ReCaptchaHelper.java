package org.swas.web.helper;

import org.swas.util.SystemParameters;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * User: GubanovAS
 * Date: 15.08.11
 * Time: 5:47
 */
@Component
public class ReCaptchaHelper {

    private ReCaptcha captcha;

    private String publicKey;
    private String privateKey;

    public ReCaptchaHelper() {
        publicKey = SystemParameters.getParam("recaptcha.key.public");
        privateKey = SystemParameters.getParam("recaptcha.key.private");
        if ("1".equals(SystemParameters.getParam("https"))) {
            captcha = ReCaptchaFactory.newSecureReCaptcha(publicKey, privateKey, false);
        } else {
            captcha = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, false);
        }
    }

    public String getCaptchaHtml(String locale) {
        Properties p = new Properties();
        p.setProperty("lang", locale);
        return captcha.createRecaptchaHtml(null, p);
    }

    public boolean checkCaptcha(String remoteAddr, String challenge, String response) {
        ReCaptchaResponse r = captcha.checkAnswer(remoteAddr, challenge, response);
        return r.isValid();
    }

    public String getPublicKey() {
        return publicKey;
    }
}
