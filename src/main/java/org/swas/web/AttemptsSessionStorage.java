package org.swas.web;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 15.02.12
 */
public class AttemptsSessionStorage {

    private static final String STORAGE = "ATTEMPTS_SESSION_STORAGE";

    private static final Long MAX_ATTEMPTS = 3l;

    private Map<String, Long> attempts = new HashMap<String, Long>();


    public static AttemptsSessionStorage forSession(HttpSession session) {
        AttemptsSessionStorage ss = (AttemptsSessionStorage) session.getAttribute(STORAGE);
        if (ss == null) {
            ss = new AttemptsSessionStorage();
            session.setAttribute(STORAGE, ss);
        }
        return ss;
    }

    public void clear(String ip) {
        attempts.remove(ip);
    }

    public void log(String ip) {
        Long current = attempts.get(ip);
        if (current == null) {
            current = 0l;

        }
        attempts.put(ip, ++current);
    }

    public boolean isGood(String ip) {
        Long current = attempts.get(ip);
        return current == null || current < MAX_ATTEMPTS;
    }
}
