package org.swas.domain;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 17.12.12
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@Entity
public class User extends BasicPersistent {

    @Column(length = 255, nullable = false, unique = true)
    @Index(name = "loginIdx")
    private String login;
    @Column(length = 255, nullable = false)
    @Index(name = "emailIdx")
    private String email;
    @Column(length = 255, nullable = false)
    private String psw;
    @Column(length = 255)
    private String name;
    private String activationCode;
    private String pswRestoreCode;
    private boolean expired;
    private boolean locked;
    private boolean passwordExpired;

    @Enumerated(EnumType.ORDINAL)
    @Index(name = "statusIdx")
    private UserStatus status;

    private String timezone;

    @Column(length = 5)
    private String language;

    @Transient
    private Locale locale;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public String getPswRestoreCode() {
        return pswRestoreCode;
    }

    public void setPswRestoreCode(String pswRestoreCode) {
        this.pswRestoreCode = pswRestoreCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Locale getLocale() {
        if (this.locale == null)
            this.locale = new Locale(this.language);
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.language = locale.getLanguage();
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
