package org.swas.domain;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public enum UserStatus {
    NEW,
    ACTIVE,
    BLOCKED;

    public static UserStatus forCode(int code) {
        for (UserStatus s : UserStatus.values()) {
            if (s.ordinal() == code)
                return s;
        }
        return null;
    }


}
