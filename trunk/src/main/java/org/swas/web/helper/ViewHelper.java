package org.swas.web.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.swas.util.MessagesHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * User: GubanovAS
 * Date: 17.08.11
 * Time: 11:13
 */
@Component
public class ViewHelper {

    public static final String VIEW_MESSAGE = "message";
    public static final String VIEW_MESSAGE_CONTENT = "message";

    public static final Set<String> LANGUAGES_RU = new HashSet<String>() {
        {
            this.add("ru");
            this.add("be");
        }
    };
    public static final SortedMap<String, String> TIMEZONES = new TreeMap<String, String>();

    static {
        for (String s : TimeZone.getAvailableIDs()) {
            TimeZone tx = TimeZone.getTimeZone(s);
            boolean negative = tx.getRawOffset() < 0;
            int hours = tx.getRawOffset() / 1000 / 60 / 60;
            if (hours < 0) hours = -hours;
            int minutes = tx.getRawOffset() / 1000 / 60 % 60;
            if (minutes < 0) minutes = -minutes;
            String n = "GMT" + (negative ? "-" : "+");
            n += (hours < 10 ? "0" + hours : hours) + ":";
            n += (minutes < 10) ? "0" + minutes : minutes;
            n += " " + s;
            if (tx.getDSTSavings() != 0)
                n += " DST";
            //DEMO
//            n = tx.getDisplayName(new Locale("ru"));
            TIMEZONES.put(n, s);
        }

    }

    public static final String VIEW_ADMIN_USER = "admin/users";


    @Autowired
    private MessagesHelper messagesHelper;
    public static final String VIEW_ADMIN = "admin/index";
    public static final String VIEW_ACCESS_DENIED = "accessDenied";


    public static boolean isCyrullicLanguage(Locale locale) {
        return ViewHelper.LANGUAGES_RU.contains(locale.getLanguage());
    }


    public String getMessageView(String code, HttpServletRequest request) {
        return getMessageView(code, new Object[]{}, request);
    }


    public String getMessageView(String code, Object[] args, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        String message = messagesHelper.getMessage(code, args, locale);
        request.setAttribute(VIEW_MESSAGE_CONTENT, message);
        return VIEW_MESSAGE;
    }

    public static String getMessageTextView(String msg, HttpServletRequest request) {
        request.setAttribute(VIEW_MESSAGE_CONTENT, msg);
        return VIEW_MESSAGE;
    }
}
