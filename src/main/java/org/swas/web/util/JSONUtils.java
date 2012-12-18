package org.swas.web.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 01.12.11
 */
public class JSONUtils {

    public static List parseJson(String data) {
        JSONParser parser = new JSONParser();
        try {
            Object o = parser.parse(data);
            if (o != null) {
                if (o instanceof List) {
                    return (List) o;
                } else if (o instanceof Map) {
                    return Collections.singletonList(o);
                }
            }
        } catch (ParseException ignored) {
            //foo
        }
        return new ArrayList<Map<String, ?>>();
    }

    public static Map parseJsonSingle(String data) {
        JSONParser parser = new JSONParser();
        try {
            Object o = parser.parse(data);
            if (o != null && o instanceof Map) {
                return (Map) o;
            }
        } catch (ParseException ignored) {
            //foo
        }
        return null;
    }

    public static String asJson(List list) {
        return JSONArray.toJSONString(list);
    }

    public static String asJson(Map map) {
        return JSONObject.toJSONString(map);
    }
}
