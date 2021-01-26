package com.comviva.proxylocation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mahindracomviva.cms.common.monitoring.MarkType;
import com.mahindracomviva.cms.common.monitoring.MeasureItem;

import com.comviva.proxylocation.kpi.KPIS;

public class ConnectionMySql {

	private static String url;
	private static String user;
	private static String psw;
	private static Integer poolSize;
	private static final Logger logger     = Logger.getLogger(ConnectionMySql.class);

	static Connection connection = null;

	public static Connection getMySqlDataSource(MeasureItem<?> measureDB) {
		
			String[] dataBaseSource;
	
			try {
				
				dataBaseSource = PropertiesReader.getMySqlConnection();
				
				url = dataBaseSource[0];
				user = dataBaseSource[1];
				psw = dataBaseSource[2];
				poolSize = Integer.valueOf(dataBaseSource[3]);
				
				while ((connection == null || connection.isClosed()) && poolSize > 0) {
					try {
						//Class.forName("com.mysql.jdbc.Driver"); 
						connection = DriverManager.getConnection(url, user, psw);
						logger.info("Connection established with dataBase");
						measureDB.mark(MarkType.OK);
					} catch (SQLException e) {
						logger.error("Error establishing connection to database");
						logger.error(e.getMessage());
						measureDB.markSpecificError("102");
						logger.debug("Mark Kpi 102 " + KPIS.dbStatus.getName());
					}
					poolSize--;
				}
			} catch (Exception e) {
				logger.error("Error establishing connection to database or reading properties");
				logger.error(e.getMessage());
				measureDB.markSpecificError("101");
				logger.debug("Mark Kpi 101 " + KPIS.dbStatus.getName());
			}
	
			return connection;
	}

}
