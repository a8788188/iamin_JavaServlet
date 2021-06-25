package com.dao.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceLocator {
    private Context initalContext;

    private static ServiceLocator serviceLocator = new ServiceLocator();

    public static ServiceLocator getInstance() {
        return serviceLocator;
    }

    // 當Constructor時，也順便new InitialContext()
    private ServiceLocator() {
        try {
            this.initalContext = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 抓取context.xml的資訊 
     * context.xml             = (Context) initalContext.lookup("java:comp/env")
     * <Resource name=XXX />   = dataSource
     * @return
     */
    public DataSource getDataSource() {
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) initalContext.lookup("java:comp/env/jdbc/plus_one");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
    
    public DataSource getDataSource(String dataSourceName) {
        DataSource datasource = null;
        try {
            Context ctx = (Context) initalContext.lookup("java:comp/env");
            datasource = (DataSource) ctx.lookup(dataSourceName);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return datasource;
    }

}
