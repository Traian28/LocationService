package com.comviva.proxylocation.utils;


import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

//import com.mysql.jdbc.Connection;
import java.sql.Connection;

public class DBCPDataSource {
    
    private static BasicDataSource ds = new BasicDataSource();
    private static final Logger logger     = Logger.getLogger(DBCPDataSource.class);
	private static String url;
	private static String user;
	private static String psw;
	private static Integer minPoolSize;
	private static Integer maxPoolSize;
	private static Integer initialSize;
	
    static {
    	
    	String[] dataBaseSource;
		try {
			dataBaseSource = PropertiesReader.getMySqlConnection();
			url = dataBaseSource[0];
			user = dataBaseSource[1];
			psw = dataBaseSource[2];
			minPoolSize = Integer.valueOf(dataBaseSource[3]);
			maxPoolSize = Integer.valueOf(dataBaseSource[4]);
			initialSize = Integer.valueOf(dataBaseSource[5]);
		} catch (IOException e) {
			logger.error("Error in DBCP getting dataBase properties");
		}
		
		//ds.setDriverClassName("com.mysql.jdbc.Driver");
			
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(psw);
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minPoolSize);
        ds.setMaxIdle(maxPoolSize);
        ds.setMaxOpenPreparedStatements(100);
        ds.setValidationQuery("SELECT 1");
        ds.setTestOnBorrow(true);
       
        //ds.setTimeBetweenEvictionRunsMillis(60*1000);
       // ds.setMinEvictableIdleTimeMillis(60*1000); // 10 minutes wait to run evictor process
        
        logger.info("Connection established with dataBase throw DBCP");
    }
    
    public static Connection getConnection() throws SQLException {
    	logger.info("Connection deliver");
    	Connection con = ds.getConnection();
    	Integer retry = 5;
    	while (con.isClosed() && retry>0) {
    		con = ds.getConnection();
    		retry--;
    	}
        return (Connection) con;
    }
    
    private DBCPDataSource(){ }
}