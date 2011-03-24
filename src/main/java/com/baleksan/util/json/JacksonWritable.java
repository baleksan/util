package com.baleksan.util.json;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface JacksonWritable {
    void writeToJson(JsonGenerator jsonGenerator, JacksonWritableConfiguration configuration) throws IOException;
}
