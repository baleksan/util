package com.baleksan.util.db;

import com.baleksan.util.conf.ConfigurationWrapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class PostgresClient {
    private static final Logger LOG = Logger.getLogger(PostgresClient.class.getName());

    public static Connection getConnection(ConfigurationWrapper conf) throws IOException {
        String dataSource = conf.getProperty(ConfigurationConstants.DB_DATASOURCE);
        assert (dataSource != null);

        String user = conf.getProperty(ConfigurationConstants.DB_USER);
        assert (user != null);

        String password = conf.getProperty(ConfigurationConstants.DB_PASSWORD);
        assert (password != null);

        return getConnection(dataSource, user, password);
    }

    /**
     * Gets a database connection using the values from the
     * configuration.
     *
     * @param datasource datasource
     * @param userName   userName
     * @param password   password
     * @return a database connection
     * @throws java.io.IOException general IoException
     */
    public static Connection getConnection(String datasource, String userName, String password) throws IOException {
        try {
            Class.forName("org.postgresql.Driver");

            LOG.info("Connecting to " + datasource + " with user '" + userName + "' and password '" + password + "'");

            Properties props = new Properties();
            props.setProperty("user", userName);
            props.setProperty("password", password);
            props.setProperty("loginTimeout", "100000");
            props.setProperty("socketTimeout", "0");

            return DriverManager.getConnection(datasource, props);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static long sqlQuerySingleLongResult(Connection connection, String sql, long valueWhenNotExist) throws SQLException {
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            rs = stmt.getResultSet();
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getLong(1);
            } else {
                return valueWhenNotExist;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static String sqlQuerySingleStringResult(Connection connection, String sql) throws SQLException {
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            rs = stmt.getResultSet();
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getString(1);
            } else {
                return null;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

}
