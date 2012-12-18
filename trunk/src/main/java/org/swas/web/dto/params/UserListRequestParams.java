package org.swas.web.dto.params;

import java.util.Date;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 19.12.11
 */
public class UserListRequestParams extends ListRequestParams {

    private String email;

    private Integer status;

    private Date createOnFrom;

    private Date createOnTo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateOnFrom() {
        return createOnFrom;
    }

    public void setCreateOnFrom(Date createOn) {
        this.createOnFrom = createOn;
    }

    public Date getCreateOnTo() {
        return createOnTo;
    }

    public void setCreateOnTo(Date createOnTo) {
        this.createOnTo = createOnTo;
    }
}
