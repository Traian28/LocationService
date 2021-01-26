package com.comviva.proxylocation.clients;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.comviva.proxylocation.utils.PropertiesReader;

public class LocationInformation {
	
	Logger logger = Logger.getLogger(LocationInformation.class);
	private Connection cn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String endpoint;
	private String secondaryEndpoint;
	private String net;
	private String address;
	private static final String validationQuery="/* ping */ SELECT 1";
	
	public LocationInformation (String imsi, String origin) {
		
		if (origin.contentEquals("hlr") || origin.contentEquals("HLR")) {
			
			setHlrInformation(imsi);
			
		} else if (origin.contentEquals("hss") || origin.contentEquals("HSS")) {
			
			setHssInformation(imsi);
			
		}
	}
	
	private void setHlrInformation(String imsi) {
		
		try {
			endpoint = PropertiesReader.getProperty("endpoint.vhlr");
			secondaryEndpoint = PropertiesReader.getProperty("endpoint.vhss");
		} catch (IOException e2) {
			logger.error("Error reading vhlr property");
		}
		
		String query = "SELECT EXS_WSDL FROM asa_external_systems WHERE EXS_PLATFORM_CODE='vhlrSrvr' ORDER BY `EXS_PRIORITY` ASC";
		
		logger.info("Getting connection to dataBase for vhlr");

		Integer retry = 5;
		
		try {
			
			cn = com.comviva.proxylocation.utils.DBCPDataSource.getConnection();
			while (!isValidConnection() && retry>0) {
				logger.debug("Getting a new connection to DB");
				cn.close();
				cn = com.comviva.proxylocation.utils.DBCPDataSource.getConnection();
				retry--;
			}
			
		} catch (SQLException e1) {
			logger.error("Error getting connection from DBCPDataSource");
			logger.error(e1.getMessage());
		} catch (Exception e1) {
			logger.error("Error getting connection from DBCPDataSource");
			logger.error(e1.getMessage());
		}

		if (cn != null) {
			
			if (endpoint==null || endpoint.contentEquals("") || endpoint.contentEquals("-") ) {
				//setting endpoint if its not declare into properties
				try { 
					pst = cn.prepareStatement(query);
					rs = pst.executeQuery();
				} catch (SQLException e) {
					logger.error("Error executing query to DataBase");
					logger.error(e.getMessage());
				}			
				try {
					if ( rs.next() ) {
						endpoint = rs.getString("EXS_WSDL");
						rs.close();
						//endpoints = endpoints + rs.getString("EXS_WSDL") +";";
					}
				} catch (SQLException e) {
					logger.error("Error reading date after query executed");
					logger.error(e.getMessage());
				}
			}
			
			if (secondaryEndpoint==null || secondaryEndpoint.contentEquals("") || secondaryEndpoint.contentEquals("-") ) {
				
				//setting secondaryendpoint
				query = "SELECT EXS_WSDL FROM asa_external_systems WHERE EXS_PLATFORM_CODE='vhssSrvr' ORDER BY `EXS_PRIORITY` ASC";
				try { 
					pst = cn.prepareStatement(query);
					rs = pst.executeQuery();
				} catch (SQLException e) {
					logger.error("Error executing query to DataBase");
					logger.error(e.getMessage());
				}			
				try {
					if ( rs.next() ) {
						secondaryEndpoint = rs.getString("EXS_WSDL");
						//endpoints = endpoints + rs.getString("EXS_WSDL") +";";
					}
				} catch (SQLException e) {
					logger.error("Error reading date after query executed");
					logger.error(e.getMessage());
				}
			}
			
			//search into 4g table to see if its register in both red
			try { 
				query = "select HLD_LTE_MME_IDENTITY from hlr_data_lte where HLD_IMSI =?";
				pst = cn.prepareStatement(query);
				pst.setString(1, imsi);
				rs = pst.executeQuery();
			} catch (SQLException e) {
				logger.error("Error executing query to DataBase");
				logger.error(e.getMessage());
			}
			try {
				if ( rs.next() ) {
					net = "hlrhss";
					rs.close();
				} else net = "hlr";
			} catch (SQLException e) {
				logger.error("Error reading date after query executed");
				logger.error(e.getMessage());
			}
			
			//setting address
			try { 
				query = "select HLD_VLR_NUMBER from hlr_data_3g where HLD_IMSI =?";
				pst = cn.prepareStatement(query);
				pst.setString(1, imsi);
				rs = pst.executeQuery();
			
			} catch (SQLException e) {
				logger.error("Error executing query to DataBase");
				logger.error(e.getMessage());
			}
			
			try {
				if ( rs.next() ) {
					address = rs.getString("HLD_VLR_NUMBER");
				}
			} catch (SQLException e) {
				logger.error("Error reading date after query executed");
				logger.error(e.getMessage());
			}
			
			finally {
				try {
					pst.close();
					rs.close();
					cn.close();
				} catch (SQLException e) {
					logger.error("Error clossing connection to DB hlr");
					logger.error(e.getMessage());
				}
	
			}
		}
		
		logger.info("Location information | address= "+address+" net= "+net+" endpoint= "+endpoint);
	}
	
	private void setHssInformation(String imsi) {
		
		try {
			endpoint = PropertiesReader.getProperty("endpoint.vhss");
			secondaryEndpoint = PropertiesReader.getProperty("endpoint.vhlr");
		} catch (IOException e2) {
			logger.error("Error reading vhss property");
		}
		
		String query = "SELECT EXS_WSDL FROM asa_external_systems WHERE EXS_PLATFORM_CODE='vhssSrvr' ORDER BY `EXS_PRIORITY` ASC";
		
		logger.info("Getting connection to dataBase for hss");

		Integer retry = 5;
		
		try {
			
			cn = com.comviva.proxylocation.utils.DBCPDataSource.getConnection();
			while (!isValidConnection() && retry>0) {
				logger.debug("Getting a new connection to DB");
				
				cn.close();
				cn = com.comviva.proxylocation.utils.DBCPDataSource.getConnection();
				retry--;
			}
			
		} catch (SQLException e1) {
			logger.error("Error getting connection from DBCPDataSource");
			logger.error(e1.getMessage());
		} catch (Exception e1) {
			logger.error("Error getting connection from DBCPDataSource");
			logger.error(e1.getMessage());
		}

		if (cn != null) {
			
			if (endpoint==null || endpoint.contentEquals("") || endpoint.contentEquals("-") ) {
			
				//setting endpoint
				try { 
					pst = cn.prepareStatement(query);
					rs = pst.executeQuery();
				} catch (SQLException e) {
					logger.error("Error executing query to DataBase");
					logger.error(e.getMessage());
				}			
				try {
					if ( rs.next() ) {
						endpoint = rs.getString("EXS_WSDL");
						//endpoints = endpoints + rs.getString("EXS_WSDL") +";";
					}
				} catch (SQLException e) {
					logger.error("Error reading date after query executed");
					logger.error(e.getMessage());
				}
			}
			
			if (secondaryEndpoint==null || secondaryEndpoint.contentEquals("") || secondaryEndpoint.contentEquals("-") ) {
				
				//setting secondaryendpoint
				query = "SELECT EXS_WSDL FROM asa_external_systems WHERE EXS_PLATFORM_CODE='vhlrSrvr' ORDER BY `EXS_PRIORITY` ASC";
				try { 
					pst = cn.prepareStatement(query);
					rs = pst.executeQuery();
				} catch (SQLException e) {
					logger.error("Error executing query to DataBase");
					logger.error(e.getMessage());
				}			
				try {
					if ( rs.next() ) {
						secondaryEndpoint = rs.getString("EXS_WSDL");
						//endpoints = endpoints + rs.getString("EXS_WSDL") +";";
					}
				} catch (SQLException e) {
					logger.error("Error reading date after query executed");
					logger.error(e.getMessage());
				}
			}
			
			//search into 3g table to see if its register in both red
			try { 
				query = "select HLD_VLR_NUMBER from hlr_data_3g where HLD_IMSI =?";
				pst = cn.prepareStatement(query);
				pst.setString(1, imsi);
				rs = pst.executeQuery();
			} catch (SQLException e) {
				logger.error("Error executing query to DataBase");
				logger.error(e.getMessage());
			}
			try {
				if ( rs.next() ) {
					net = "hlrhss";
				} else net = "hss";
			} catch (SQLException e) {
				logger.error("Error reading date after query executed");
				logger.error(e.getMessage());
			}
			
			//setting address
			try { 
				query = "select HLD_LTE_MME_IDENTITY from hlr_data_lte where HLD_IMSI =?";
				pst = cn.prepareStatement(query);
				pst.setString(1, imsi);
				rs = pst.executeQuery();
				
			} catch (SQLException e) {
				logger.error("Error executing query to DataBase");
				logger.error(e.getMessage());
			}
			
			try {
				if ( rs.next() ) {
					address = rs.getString("HLD_LTE_MME_IDENTITY");
				}
			} catch (SQLException e) {
				logger.error("Error reading date after query executed");
				logger.error(e.getMessage());
			}
			
			finally {
				try {
					pst.close();
					rs.close();
					cn.close();
				} catch (SQLException e) {
					logger.error("Error clossing connection to DB hlr");
					logger.error(e.getMessage());
				}
	
			}
		}
		
		logger.info("Location information | address= "+address+" net= "+net+" endpoint= "+endpoint);
	}
	
	private boolean isValidConnection() {

		boolean isValid = false;

		try {
			pst = cn.prepareStatement(validationQuery);
			rs = pst.executeQuery();
			if (rs.next()) {
				isValid = true;
				logger.debug("Connection OK");
				}
			else {
				isValid = false;
				logger.error("No valid connection to database");
			}
		} catch (SQLException e) {
			logger.error("No valid connection to database");
			return false;
		}

		return isValid;

	}

	public String getAddress () {
		
		return this.address;
	}
	
	public String getNet () {
		
		return this.net;
	}
	
	public String getEndpoint() {
		
		return this.endpoint;
	}
	
	public String getSecondaryEndpoint() {
		
		return this.secondaryEndpoint;
	}
	
}
