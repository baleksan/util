package com.baleksan.util.json;

import org.codehaus.jackson.JsonNode;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface JacksonReadable {
    void fromJsonTreeModel(JsonNode root);
}
