package com.ct.ks.bsc.qte.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ct.ks.bsc.qte.core.MasterCrudHandler;
import com.ct.ks.bsc.qte.db.DataSourcePool;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MasterCrudHandler.getInstance().initMasterDbIfNotExist();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // shutdown all db connection pools
        DataSourcePool.shutdown();
    }

}
