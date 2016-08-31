package com.ct.ks.bsc.qte.util.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.ct.ks.bsc.qte.core.Constants;

public class DataSourcePool {

    private static final Map<String, DataSource> dss = new HashMap<String, DataSource>();
    private static final String DBCP_PROPERTIES_FILENAME = "dbcp.properties";


    public static DataSource getDataSource(String dsName) throws Exception {
        DataSource ds = dss.get(dsName);
        if (Constants.MASTER_DATASOURCE_NAME == dsName) {
            return getMasterDataSource();
        } else if (null == ds) {
            Properties p = new Properties();
            p.load(DataSourcePool.class.getClassLoader().getResourceAsStream(DBCP_PROPERTIES_FILENAME));
            // driverClassName=
            // url=
            // username=
            // password=
            ds = BasicDataSourceFactory.createDataSource(p);
            dss.put(dsName, ds);
        }
        return ds;
    }


    public static DataSource getMasterDataSource() throws Exception {
        DataSource ds = dss.get(Constants.MASTER_DATASOURCE_NAME);
        if (null == ds) {
            Properties p = new Properties();
            p.load(DataSourcePool.class.getClassLoader().getResourceAsStream(DBCP_PROPERTIES_FILENAME));
            p.setProperty("driverClassName", Constants.CONFIG_H2_JDBC_DRIVER);
            p.setProperty("url", Constants.CONFIG_H2_JDBC_URL);
            ds = BasicDataSourceFactory.createDataSource(p);
            dss.put(Constants.MASTER_DATASOURCE_NAME, ds);
        }
        return ds;
    }


    public static Connection getConnection(String dsName) throws Exception {
        return getDataSource(dsName).getConnection();
    }


    public static Connection getMasterConnection() throws Exception {
        return getConnection(Constants.MASTER_DATASOURCE_NAME);
    }


    public static void close(String dsName) throws SQLException {
        DataSource ds = dss.get(dsName);
        if (null != ds) {
            try {
                ((BasicDataSource) ds).close();
            } finally {
                dss.remove(dsName);
            }
        }
    }


    public static void closeAll() throws SQLException {
        for (DataSource ds : dss.values()) {
            ((BasicDataSource) ds).close();
        }
        dss.clear();
    }
}
