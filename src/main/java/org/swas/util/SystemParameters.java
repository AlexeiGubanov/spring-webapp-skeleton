package org.swas.util;

import java.util.ResourceBundle;

/**
 * User: GubanovAS
 * Date: 07.08.11
 * Time: 10:25
 * TODO realize via Properties
 */

public class SystemParameters {

    private static final ResourceBundle PARAMETERS;

    public static final String LOCALE;

    static {
        PARAMETERS = ResourceBundle.getBundle(
                "properties.system");
        LOCALE = getParam("locale");
    }

    public static String getParam(String name) {
        return PARAMETERS.getString(name);
    }
}
