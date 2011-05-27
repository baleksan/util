package com.baleksan.util.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    public static String toJson(JacksonWritable jacksonWritable) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(writer);
        JacksonWritableConfiguration jsonConf = new JacksonWritableConfiguration(false);
        try {
            jacksonWritable.writeToJson(jsonGenerator, jsonConf);
        } finally {
            jsonGenerator.close();
        }

        return writer.toString();
    }

    public static void fromJson(String json, JacksonReadable readable) throws IOException {
        StringReader reader = new StringReader(json);
        JsonNode root = mapper.readTree(reader);
        readable.fromJsonTreeModel(root);
    }

    public static String toJson(Object object) throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, object);
        return writer.toString();
    }
}
