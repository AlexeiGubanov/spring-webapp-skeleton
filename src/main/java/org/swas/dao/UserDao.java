package org.swas.dao;

import org.swas.domain.User;

/**
 * Sample custom dao extending of {@link GenericDao}
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public interface UserDao extends GenericDao<User, Long> {

    /**
     * Find user with specified login
     *
     * @param login login
     * @return User instance if found, null otherwise
     */
    User findByLogin(String login);


    /**
     * Find user with specified activation code
     *
     * @param code activation code
     * @return User instance if found, null otherwise
     */
    User findByActivationCode(String code);

    /**
     * Find user with specified email
     *
     * @param email email
     * @return User instance if found, null otherwise
     */
    User findByEmail(String email);


    /**
     * Checks if User with specified login exists in datasource
     * @param login login
     * @return true if exists
     */
    boolean isLoginExists(String login);

    /**
     * Checks if User with specified email exists in datasource
     * @param email email
     * @return true if exists
     */
    boolean isEmailExists(String email);

}
