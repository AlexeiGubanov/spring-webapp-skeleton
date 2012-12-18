package org.swas.web.dto.form;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 07.12.11
 */
public class RestorePasswordForm {

    @NotBlank
    private String identity;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
