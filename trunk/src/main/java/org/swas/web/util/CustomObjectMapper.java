package org.swas.web.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 05.12.11
 */
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        this.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
    }
}
