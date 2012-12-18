package org.swas.web.helper;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 *         TODO: clean after change
 */
public class UrlHelper {

    public static final String URL_USER_VIEW = "/admin/users";
    public static final String URL_SIGNUP = "/signUp";
    public static final String URL_ACTIVATE = "/activate";
    public static final String URL_SIGNIN = "/signIn";
    public static final String URL_PASSWORD_RESTORE = "/restorePassword";

    public static final String URL_USER_LIST = "/admin/user/list";
    public static final String URL_USER_VIEW_UPDATE = "/admin/user/update";

    /* **********************************************************************/
    public static final String URL_SIGNOUT = "/signOut";
    public static final String URL_ACCESS_DENIED = "/accessDenied";
    public static final String URL_ADMIN_VIEW = "/admin";

    public static final String[] AJAX_URLS = new String[]{
            URL_SIGNUP,
            URL_SIGNIN,
            URL_PASSWORD_RESTORE,
            URL_USER_LIST

    };


}
