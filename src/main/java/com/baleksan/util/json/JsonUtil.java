package com.baleksan.util.json;

import com.baleksan.util.json.JacksonWritable;
import com.baleksan.util.json.JacksonWritableConfiguration;
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
        ObjectMapper mapper = new ObjectMapper();
        StringReader reader = new StringReader(json);
        JsonNode root = mapper.readTree(reader);
        readable.fromJsonTreeModel(root);
    }
}
