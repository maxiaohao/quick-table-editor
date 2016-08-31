package com.ct.ks.bsc.qte.core;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ct.ks.bsc.qte.util.db.DataSourcePool;
import com.ct.ks.bsc.qte.util.db.SqlRunner;

public class DefaultHandler {

    public static List<String> listTables(String schema){

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
