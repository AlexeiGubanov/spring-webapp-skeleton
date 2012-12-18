package org.swas.service;

import org.swas.domain.User;

import java.util.Locale;

/**
 * Sample service for user domain class.
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public interface UserService extends GenericDaoService<User, Long> {

    /**
     * Return codes
     */
    int USER_NOT_FOUND = 100;
    int USER_NOT_ACTIVATED = 101;
    int USER_ALREADY_ACTIVATED = 102;
    int USER_INCORRECT_PASSWORD = 105;
    int USER_INCORRECT_SECRET_ANSWER = 106;
    int USER_INCORRECT_SECRET_QUESTION = 107;

    Result register(User user, Locale locale, ActivationUrlGenerator activationUrlGenerator);

    Result activate(String activationCode);

    Result authorize(String login, String password);

    Result passwordRestore(String identity, Locale locale);

    boolean isLoginExists(String login);

    boolean isEmailExists(String email);

    Result update(User trans);

}
