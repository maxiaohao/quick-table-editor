package com.ct.ks.bsc.qte.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ct.ks.bsc.qte.db.DataSourcePool;
import com.ct.ks.bsc.qte.db.SqlRunner;
import com.ct.ks.bsc.qte.model.ColumnInfo;
import com.ct.ks.bsc.qte.model.QteDataSource;
import com.ct.ks.bsc.qte.util.CrudResult;

public class CoreHandler {

    /**
     * log writer for this class
     */
    private final Logger log = LoggerFactory.getLogger(CoreHandler.class);

    private static final CoreHandler inst = new CoreHandler();


    private CoreHandler() {

    }


    public static CoreHandler getInstance() {
        return inst;
    }


    public CrudResult validateQteDataSource(QteDataSource ds) {
        CrudResult ret = null;

        return ret;
    }


    private synchronized void reloadJdbcJars() throws IOException {
        File jdbcPath = new File(Constants.EXTRA_JDBC_LIB_DIR);
        if (!jdbcPath.isDirectory()) {
            boolean mkdirs = jdbcPath.mkdirs();
            if (!mkdirs) {
                throw new IOException("failed to create jdbc lib path " + Constants.EXTRA_JDBC_LIB_DIR);
            }
        }

        File[] jars = jdbcPath.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return null != name && name.toLowerCase().endsWith(".jar");
            }
        });

        if (null != jars) {
            for (File jar : jars) {
                JdbcClassLoader.getInstance().addJar(jar);
            }
        }

    }


    public synchronized CrudResult validateDataSource(QteDataSource ds) {
        // basic checks
        if (null == ds) {
            return new CrudResult(false, "datasource is invalid");
        } else if (ds.getDs_name() == null || ds.getDs_name().length() == 0) {
            return new CrudResult(false, "datasource name for the datasource should not be empty");
        } else if (ds.getJdbc_class() == null || ds.getJdbc_class().length() == 0) {
            return new CrudResult(false, "jdbc class name for the datasource '" + ds.getDs_name()
                    + "' should not be empty");
        } else if (ds.getJdbc_url() == null || ds.getJdbc_url().length() == 0) {
            return new CrudResult(false, "jdbc url for the datasource '" + ds.getDs_name() + "' should not be empty");
        }

        // reload the jars in jdbc lib dir (in case of any dynamically put new jar file)
        try {
            reloadJdbcJars();
        } catch (IOException e1) {
            return new CrudResult(false, "Error occurred while reloading jdbc jars: " + e1.getLocalizedMessage());
        }

        // check jdbc driver class name
        try {
            Class.forName(ds.getJdbc_class(), true, JdbcClassLoader.getInstance());
        } catch (ClassNotFoundException e) {
            return new CrudResult(false, "failed to load class (" + ds.getJdbc_class() + ") : "
                    + e.getLocalizedMessage());
        }

        // check connection using the jdbc url
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(ds.getJdbc_url(), ds.getUsername(), ds.getPassword());
        } catch (SQLException se) {
            return new CrudResult(false, "failed to establish connection with jdbc url (" + ds.getJdbc_url() + "): "
                    + se.getLocalizedMessage());
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    return new CrudResult(false, "error occurred while closing jdbc connection ("
                            + ds.getJdbc_url() + "): " + e.getLocalizedMessage());
                }
            }
        }

        return new CrudResult(true);
    }


    public CrudResult validateTable(QteDataSource ds, String schema, String table) {

        // validate params
        if (null == schema || schema.length() == 0) {
            return new CrudResult(false, "schema should not be empty");
        } else if (null == table || table.length() == 0) {
            return new CrudResult(false, "table should not be empty");
        }

        // validate ds
        CrudResult dsCheckResult = validateDataSource(ds);
        if (!dsCheckResult.isSuccess()) {
            return dsCheckResult;
        }

        // validate if the table exists
        String sql = "select count(*) from " + schema + "." + table;
        try {
            SqlRunner.getInstance(ds.getDs_name()).query1stCell(sql);
        } catch (Exception e) {
            return new CrudResult(false, "failed to validate table by running sql: " + sql + " (error message: "
                    + e.getLocalizedMessage() + ")");
        }

        // check if primary key exists in the table
        boolean hasPk = false;
        try {
            List<ColumnInfo> colInfo = getColumnInfo(ds.getDs_name(), schema, table);
            for (ColumnInfo c : colInfo) {
                if (null != c && c.isPrimary_key()) {
                    hasPk = true;
                }
            }
        } catch (SQLException e) {
            return new CrudResult(false, "error occurred while retrieving column info from table (" + table + "): "
                    + e.getLocalizedMessage());
        }

        if (hasPk) {
            return new CrudResult(true);
        } else {
            return new CrudResult(false, "table for editing should have primary key");
        }

    }


    protected List<String> getSchemaNames(String dsName) throws SQLException {
        List<String> ret = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = DataSourcePool.getConnection(dsName);
            DatabaseMetaData m = conn.getMetaData();
            ResultSet rs = m.getSchemas();
            String colSchemaName = ColumnInfo.getActualColName(rs.getMetaData(),
                    Constants.META_COL_TYPICAL_NAME_TABLE_SCHEM);
            while (rs.next()) {
                ret.add(rs.getString(colSchemaName));
            }
            rs.close();
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }


    protected List<String> getTableNames(String dsName, String schemaName) throws SQLException {
        List<String> ret = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = DataSourcePool.getConnection(dsName);
            DatabaseMetaData m = conn.getMetaData();
            ResultSet rs = m.getTables(null, schemaName, null, null);
            String colNameTable = ColumnInfo.getActualColName(rs.getMetaData(),
                    Constants.META_COL_TYPICAL_NAME_TABLE_NAME);
            while (rs.next()) {
                ret.add(rs.getString(colNameTable));
            }
            rs.close();
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }


    protected List<ColumnInfo> getColumnInfo(String dsName, String schemaName, String tableName) throws SQLException {
        List<ColumnInfo> ret = new ArrayList<ColumnInfo>();
        Connection conn = null;
        try {
            conn = DataSourcePool.getConnection(dsName);
            DatabaseMetaData m = conn.getMetaData();

            String pkColumnName = null;
            ResultSet rsPk = m.getPrimaryKeys(null, schemaName, tableName);
            while (rsPk.next()) {
                pkColumnName = rsPk.getString(Constants.META_COL_TYPICAL_NAME_COLUMN_NAME);
            }
            rsPk.close();

            ResultSet rsCols = m.getColumns(null, schemaName, tableName, null);
            ResultSetMetaData rsm = rsCols.getMetaData();

            while (rsCols.next()) {
                ColumnInfo colInf = new ColumnInfo();
                colInf.setTable_schem(rsCols.getString(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_TABLE_SCHEM)));
                colInf.setTable_name(rsCols.getString(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_TABLE_NAME)));
                colInf.setColumn_name(rsCols.getString(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_COLUMN_NAME)));
                colInf.setData_type(rsCols.getInt(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_DATA_TYPE)));
                colInf.setType_name(rsCols.getString(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_TYPE_NAME)));
                colInf.setColumn_size(rsCols.getInt(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_COLUMN_SIZE)));
                colInf.setDecimal_digits(rsCols.getInt(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_DECIMAL_DIGITS)));
                colInf.setNullable(rsCols.getBoolean(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_NULLABLE)));
                colInf.setRemarks(rsCols.getString(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_REMARKS)));
                colInf.setColumn_def(rsCols.getObject(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_COLUMN_DEF)));
                colInf.setAutoincrement(rsCols.getBoolean(ColumnInfo.getActualColName(rsm,
                        Constants.META_COL_TYPICAL_NAME_IS_AUTOINCREMENT)));
                if (colInf.getColumn_name() == pkColumnName) {
                    colInf.setPrimary_key(true);
                }
                ret.add(colInf);
            }

            rsCols.close();
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SQLException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {

    }
}
