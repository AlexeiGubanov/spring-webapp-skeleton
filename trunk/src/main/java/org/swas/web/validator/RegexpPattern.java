package org.swas.web.validator;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class RegexpPattern {
    public static final String EMAIL = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$";
    public static final String PHONE = "^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$";
    public static final String DATE = "^(((0?[1-9]|[12]\\d|3[01])[\\.\\-\\/](0?[13578]|1[02])"
            + "[\\.\\-\\/]((1[6-9]|[2-9]\\d)?\\d{2}))|((0?[1-9]|[12]\\d"
            + "|30)[\\.\\-\\/](0?[13456789]|1[012])[\\.\\-\\/]((1[6-9]|"
            + "[2-9]\\d)?\\d{2}))|((0?[1-9]|1\\d|2[0-8])[\\.\\-\\/]0?2["
            + "\\.\\-\\/]((1[6-9]|[2-9]\\d)?\\d{2}))|(29[\\.\\-\\/]0?2["
            + "\\.\\-\\/]((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579]["
            + "26])|((16|[2468][048]|[3579][26])00)|00)))$";
    public static final String NUMERIC = "[+-][0-9]+";
    public static final String POSITIVE = "[+]?[0-9]+";
    public static final String NEGATIVE = "-[0-9]+";
}
