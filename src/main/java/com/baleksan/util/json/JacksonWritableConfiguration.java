package com.baleksan.util.json;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class JacksonWritableConfiguration {
    private boolean jsonReducted;

    public JacksonWritableConfiguration(boolean jsonReducted) {
        this.jsonReducted = jsonReducted;
    }

    public boolean isJsonReducted() {
        return jsonReducted;
    }

    public void setJsonReducted(boolean jsonReducted) {
        this.jsonReducted = jsonReducted;
    }
}
