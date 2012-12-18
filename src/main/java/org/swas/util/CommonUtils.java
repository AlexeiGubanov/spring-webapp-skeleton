package org.swas.util;

import org.swas.domain.BasicPersistent;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.List;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
public class CommonUtils {

    public static final String[] invalidFileNameChars = {
            "/",
            "|",
            "\\",
            "?",
            ":",
            ";"
    };

    /**
     * <p/>
     * Prepare file name in correct format: delete invalid chars.
     */
    public static String prepareFileName(String name) {
        name = name.trim();
        for (String s : invalidFileNameChars) {
            name = name.replace(s, "");
        }
        return name;
    }


    /**
     * <p/>
     * Prepare string as uri:  using Unicode NFD, replace spaces with underscores, convert to lower case
     */
    public static String slugify(String input) {
        if (input == null || input.length() == 0) return "";
        String toReturn = normalize(input);
        toReturn = toReturn.replaceAll("\\s+", "_");
        toReturn = toReturn.toLowerCase();
        return toReturn;
    }

    private static String normalize(String input) {
        if (input == null || input.length() == 0) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD);
    }

    /**
     * <p/>
     * Converts list of {@code BasicPersistent} to their ids array
     */
    public static Long[] asIdsArray(List<? extends BasicPersistent> source) {
        Long[] res = new Long[source.size()];
        int i = 0;
        for (BasicPersistent e : source) {
            res[i++] = e.getId();
        }
        return res;

    }


    /**
     * Cast object to number, where object may be {@link Number} instance or a {@link String} instance.
     * If object nether is Number or String, returns null
     *
     * @param o - object
     * @return object casted to Number, or null
     */
    public static Number asNumber(Object o) {
        if (o == null)
            return null;
        if (o instanceof Number) {
            return (Number) o;
        } else if (o instanceof String) {
            try {
                return NumberUtils.createNumber((String) o);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Cast object to Integer, where object may be {@link Number} instance or a {@link String} instance.
     * If object neither is Number or String, returns null
     *
     * @param o - object
     * @return object casted to Integer, or null
     */
    public static Integer asInteger(Object o) {
        if (o == null)
            return null;
        if (o instanceof Number) {
            return ((Number) o).intValue();
        } else if (o instanceof String) {
            try {
                return NumberUtils.createInteger((String) o);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Cast object to Long, where object may be {@link Number} instance or a {@link String} instance.
     * If object nether is Number or String, returns null
     *
     * @param o - object
     * @return object casted to Long, or null
     */
    public static Long asLong(Object o) {
        if (o == null)
            return null;
        if (o instanceof Number) {
            return ((Number) o).longValue();
        } else if (o instanceof String) {
            try {
                return NumberUtils.createLong((String) o);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Cast object to Double, where object may be {@link Number} instance or a {@link String} instance.
     * If object nether is Number or String, returns null
     *
     * @param o - object
     * @return object casted to Double, or null
     */
    public static Double asDouble(Object o) {
        if (o == null)
            return null;
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof String) {
            try {
                return NumberUtils.createDouble((String) o);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * <p/>
     * Return server prefix based on system params and current context
     */
    public static String getServerPrefix(HttpServletRequest request) {
        String template = SystemParameters.getParam("base.url");
        String serverName = request.getServerName();
        if (request.getServerPort() != 80) {
            serverName += ":" + request.getServerPort();
        }
        template = template.replaceAll("\\{server\\}", serverName);
        template = template.replaceAll("\\{context\\}", request.getContextPath());
        return template;
    }
}
