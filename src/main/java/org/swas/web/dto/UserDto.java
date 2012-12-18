package org.swas.web.dto;

import org.swas.domain.User;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 19.12.11
 */
public class UserDto {

    private Long id;
    private String email;
    private Integer status;
    private String timezone;
    private Integer role;

    public UserDto() {

    }

    public UserDto(User u) {
        this.id = u.getId();
        this.email = u.getEmail();
        this.status = u.getStatus().ordinal();
        this.timezone = u.getTimezone();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
