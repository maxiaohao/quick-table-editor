package com.ct.ks.bsc.qte.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ct.ks.bsc.qte.core.Constants;
import com.ct.ks.bsc.qte.core.MasterCrudHandler;
import com.ct.ks.bsc.qte.model.QteDataSource;

public class DataSourcePool {

    private static Logger log = LoggerFactory.getLogger(DataSourcePool.class);

    private static final Map<String, DataSource> dss = new HashMap<>();
    private static final String DBCP_PROPERTIES_FILENAME = "dbcp.properties";


    public static DataSource getDataSource(String dsName) throws Exception {
        DataSource ds = dss.get(dsName);
        if (Constants.MASTER_DATASOURCE_NAME == dsName) {
            return getMasterDataSource();
        } else if (null == ds) {
            Properties p = new Properties();
            p.load(DataSourcePool.class.getClassLoader().getResourceAsStream(DBCP_PROPERTIES_FILENAME));
            QteDataSource qteDs = MasterCrudHandler.getInstance().getQteDataSource(dsName);
            if (null == qteDs) {
                throw new Exception("failed to get datasource conf info from master db (dsName=" + dsName + ")");
            }
            p.setProperty("driverClassName", qteDs.getJdbc_class());
            p.setProperty("url", qteDs.getJdbc_url());
            p.setProperty("username", qteDs.getUsername());
            p.setProperty("password", qteDs.getPassword());
            ds = BasicDataSourceFactory.createDataSource(p);
            dss.put(dsName, ds);
            log.warn("Datasource '{}'(jdbc driver='{}', url='{}') created.", dsName, qteDs.getJdbc_class(),
                    qteDs.getJdbc_url());
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
            log.warn("Datasource '{}' (driver='{}', url='{}') created.", Constants.MASTER_DATASOURCE_NAME,
                    Constants.CONFIG_H2_JDBC_DRIVER, Constants.CONFIG_H2_JDBC_URL);
        }
        return ds;
    }


    public static Connection getConnection(String dsName) throws Exception {
        return getDataSource(dsName).getConnection();
    }


    public static Connection getMasterConnection() throws Exception {
        return getConnection(Constants.MASTER_DATASOURCE_NAME);
    }


    public static void close(String dsName) {
        DataSource ds = dss.get(dsName);
        if (null != ds) {
            try {
                ((BasicDataSource) ds).close();
                log.warn("Datasource '{}' closed.", dsName);
            } catch (SQLException e) {
                log.error("Error occurred while closing datasource '{}': {}", dsName, e.getLocalizedMessage());
            } finally {
                dss.remove(dsName);
            }
        }
    }


    public static void shutdown() {
        Set<String> jdbcDriverUrls = new HashSet<>();
        for (String dsName : dss.keySet()) {
            try {
                BasicDataSource ds = (BasicDataSource) dss.get(dsName);
                jdbcDriverUrls.add(ds.getUrl());
                ds.close();
                log.warn("Datasource '{}' closed.", dsName);
            } catch (SQLException e) {
                log.error("Error occurred while closing datasource '{}': {}", dsName, e.getLocalizedMessage());
            }
        }
        // also deregister all the jdbc drivers loaded by pool
        for (String url : jdbcDriverUrls) {
            try {
                Driver drv = DriverManager.getDriver(url);
                DriverManager.deregisterDriver(drv);
                log.warn("JDBC driver '{}' (url:'{}') deregistered.", drv.getClass().getName(), url);
            } catch (SQLException e) {
                log.error("Error occurred while deregistering JDBC driver: '{}', error: {}", url,
                        e.getLocalizedMessage());
            }
        }
        dss.clear();
    }
}
