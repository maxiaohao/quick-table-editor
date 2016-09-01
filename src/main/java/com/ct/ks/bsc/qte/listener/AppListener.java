package com.ct.ks.bsc.qte.listener;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ct.ks.bsc.qte.core.Constants;
import com.ct.ks.bsc.qte.db.DataSourcePool;
import com.ct.ks.bsc.qte.db.SqlRunner;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // init h2 config db if not exists
        try {
            SqlRunner runner = SqlRunner.getMasterInstance();
            Number cnt = (Number) runner.query1stCell(Constants.SQL_GET_TABLE_COUNT);
            if (cnt.intValue() == 0) {
                runner.exec("RUNSCRIPT FROM 'classpath:" + Constants.INIT_DB_SQL_FILENAME + "'");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // shutdown all db connection pools
        try {
            DataSourcePool.closeAll();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
