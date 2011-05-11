package com.baleksan.util.conf;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class ConfigurationWrapper {
    private static final Logger LOG = Logger.getLogger(ConfigurationWrapper.class.getName());

    private CompositeConfiguration config;

    public ConfigurationWrapper(String configurationFileName, String defaultConfigurationFileName) throws ConfigurationException {
        config = new CompositeConfiguration();
        try {
            File configFile = new File(configurationFileName);
            XMLConfiguration defaults = new XMLConfiguration();
            XMLConfiguration local = new XMLConfiguration();
            InputStream is = ConfigurationWrapper.class.getClassLoader().getResourceAsStream(defaultConfigurationFileName);
            defaults.load(is);
            is.close();
            if (configFile.exists()) {
                is = new BufferedInputStream(new FileInputStream(configFile));
                local.load(is);
                is.close();
            }
            config.addConfiguration(local);
            config.addConfiguration(defaults);
        } catch (Exception e) {
            LOG.error(e);
            throw new ConfigurationException(e);
        }
    }

    public ConfigurationWrapper() {
        config = new CompositeConfiguration();
    }

    public String getProperty(String propertyName, String defaultValue) {
        String value = config.getString(propertyName);
        if (value != null) {
            return value;
        }

        return defaultValue;
    }

    public String getProperty(String propertyName) {
        return config.getString(propertyName);
    }

    public boolean hasProperty(String propertyName) {
        return config.getString(propertyName) != null;
    }

    public int getPropertyAsInt(String propertyName) {
        return config.getInt(propertyName);
    }

    public int getPropertyAsInt(String propertyName, int defaultValue) {
        int value = config.getInt(propertyName);
        if (value != 0) {
            return value;
        }

        return defaultValue;
    }

    public long getPropertyAsLong(String propertyName, long defaultValue) {
        long value = config.getLong(propertyName);
        if (value != 0) {
            return value;
        }

        return defaultValue;
    }

    public String[] getPropertyAsStringArray(String propertyName) {
        String valueEncoded = getProperty(propertyName);

        if (valueEncoded == null || valueEncoded.isEmpty()) {
            return null;
        }

        String[] splits = valueEncoded.split(";");
        for (int i = 0; i < splits.length; i++) {
            splits[i] = splits[i].trim();
        }

        return splits;
    }

    public String[] getPropertyAsStringArray(String propertyName, String defaultValue) {
        String valueEncoded = getProperty(propertyName, defaultValue);

        if (valueEncoded == null || valueEncoded.isEmpty()) {
            return new String[0];
        }

        String[] splits = valueEncoded.split(";");
        for (int i = 0; i < splits.length; i++) {
            splits[i] = splits[i].trim();
        }

        return splits;
    }

    public void setProperty(String propertyName, int value) {
        config.setProperty(propertyName, value);
    }

    public void setProperty(String propertyName, String value) {
        config.setProperty(propertyName, value);
    }

    public Map<String, String> getPropertyAsMap(String propertyName) throws ConfigurationException {
        // encoding key:value;key:value
        String encoding = getProperty(propertyName, "");
        String[] kvPairs = encoding.split(";");

        Map<String, String> map = new HashMap<String, String>();
        for (String kv : kvPairs) {
            String[] splits = kv.split(":");
            if (splits.length != 2) {
                throw new ConfigurationException("Incorrect encoding of the map property " + propertyName);
            }

            map.put(splits[0], splits[1]);
        }

        return map;
    }

    public boolean getPropertyAsBoolean(String propertyName, boolean defaultValue) {
        return config.getBoolean(propertyName, defaultValue);
    }

    public int getNumberProperties() {
        return getPropertyNames().size();
    }

    public List<String> getPropertyNames() {
        List<String> properties = new ArrayList<String>();
        Iterator it = config.getKeys();
        while (it.hasNext()) {
            properties.add((String) it.next());
        }

        return properties;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getNumberProperties());
        builder.append(" props in ");
        builder.append(config.getNumberOfConfigurations());
        builder.append(" configs.");


        return builder.toString();
    }
}
