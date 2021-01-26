package com.comviva.proxylocation.proxy;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ProxyMain {
	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(ProxyMain.class);
		//BasicConfigurator.configure();
		PropertyConfigurator.configure("log4j.properties");
		
		logger.info("ProxyLocationStarted");
		
		ProxyLocationWebService webService = new ProxyLocationWebService();
		
		webService.startServer();
		
	}

}
