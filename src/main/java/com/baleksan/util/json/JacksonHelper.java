package com.baleksan.util.json;

import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class JacksonHelper {

    public static List<String> getArrayNodeAsStringList(JsonNode arrayNode) {
        List<String> results = new ArrayList<String>();
        for (final JsonNode arrayElementNode : arrayNode) {
            results.add(arrayElementNode.getTextValue());

        }

        return results;
    }

    public static String getArrayNodeAsString(JsonNode arrayNode, String separator) {
        StringBuilder builder = new StringBuilder();
        List<String> values = getArrayNodeAsStringList(arrayNode);
        for (int i = 0; i < values.size(); i++) {
            builder.append(values.get(i));
            if (i + 1 < values.size()) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }
}
