package com.ct.ks.bsc.qte.core;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ct.ks.bsc.qte.db.DataSourcePool;
import com.ct.ks.bsc.qte.db.SqlRunner;
import com.ct.ks.bsc.qte.model.QteDataSource;
import com.ct.ks.bsc.qte.util.CrudResult;

public class DefaultHandler {

    private static final DefaultHandler inst = new DefaultHandler();

    static Method addUrlMethod = null;
    static {
        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean accessible = addUrlMethod.isAccessible(); // 获取方法的访问权限
            try {
                if (accessible == false) {
                    addUrlMethod.setAccessible(true); // 设置方法的访问权限
                }

            } finally {
                addUrlMethod.setAccessible(accessible);
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private DefaultHandler() {

    }


    public static DefaultHandler getInstance() {
        return inst;
    }


    public CrudResult validateQteDataSource(QteDataSource ds) {
        CrudResult ret = null;

        return ret;
    }


    public CrudResult validateTable(QteDataSource ds, String schema, String table) {
        File jdbcPath = new File(Constants.EXTRA_JDBC_LIB_DIR);
        if (!jdbcPath.isDirectory()) {
            boolean mkdirs = jdbcPath.mkdirs();
            if (!mkdirs) {
                return new CrudResult(false, "failed to create jdbc lib path " + Constants.EXTRA_JDBC_LIB_DIR);
            }
        }

        File[] jars = jdbcPath.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return null != name && name.toLowerCase().endsWith(".jar");
            }
        });

        if (null != jars) {

            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            for (File jar : jars) {
                URL url = null;
                try {
                    url = jar.toURI().toURL();
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                try {
                    addUrlMethod.invoke(cl, url);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            return new CrudResult(true);
        }
        return new CrudResult(false, "failed to create jdbc lib path " + Constants.EXTRA_JDBC_LIB_DIR);

        // Class.forName(ds.getJdbc_class(), true, this.getClass().getClassLoader());
        // new URLClassLoader();

    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // URLClassLoader child = new URLClassLoader (myJar.toURL(), this.getClass().getClassLoader());
        // Class classToLoad = Class.forName ("com.MyClass", true, child);

        // Class.forName(Constants.CONFIG_H2_JDBC_DRIVER);
        // Connection conn = DriverManager.getConnection(Constants.CONFIG_H2_JDBC_URL);
        // conn.close();

        try {
            SqlRunner runner = SqlRunner.getMasterInstance();
            Number cnt = (Number) runner.query1stCell(Constants.SQL_GET_TABLE_COUNT);
            if (cnt.intValue() == 0) {
                runner.exec("RUNSCRIPT FROM 'classpath:" + Constants.INIT_DB_SQL_FILENAME + "'");
            }

            DatabaseMetaData md = DataSourcePool.getMasterConnection().getMetaData();
            ResultSet rs = md.getColumns(null, null, "FOO", null);
            // ResultSet rs = md.getPrimaryKeys(null, null, "QTE_T_TABLE");
            final int cols = rs.getMetaData().getColumnCount();

            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++) {
                colNames[i] = rs.getMetaData().getColumnName(i + 1);
            }

            List<Object[]> result = new ArrayList<Object[]>();
            Object[] arow = null;
            while (rs.next()) {
                arow = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    arow[i] = rs.getObject(i + 1);
                }
                result.add(arow);
            }
            rs.close();

            System.out.println();

            for (String col : colNames) {
                System.out.printf("%30s", col);
            }
            System.out.println();
            for (Object[] row : result) {
                for (int i = 0; i < cols; i++) {
                    System.out.printf("%30s", row[i]);
                }
                System.out.println();
            }

            System.out.println();

            for (String col : colNames) {
                System.out.println(col);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
