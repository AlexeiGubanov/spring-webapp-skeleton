package org.swas.web.util;

import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
public class MapValueExtractor implements ValueExtractor {

    private Map<String, ?> map;

    public MapValueExtractor(Map<String, ?> map) {
        if (map == null)
            throw new IllegalStateException("Param 'map' can't be null");
        this.map = map;
    }

    public Object extractValue(String name) {
        return map.get(name);
    }
    
    
}
