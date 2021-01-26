package com.comviva.proxylocation.proxy;

import java.io.IOException;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.comviva.proxylocation.kpi.ProxyLocationKpiApplication;
import com.comviva.proxylocation.utils.PropertiesReader;

public class ProxyLocationWebService {
	private ProxyLocationImpl proxyLocationImpl;
	private Endpoint endpoint;
	private String endpointAddress;
	Logger logger = Logger.getLogger(ProxyLocationWebService.class);
	
	/** Medicion de Kpi*/
	private ProxyLocationKpiApplication kpiMeasure = ProxyLocationKpiApplication.getInstance();
	
	public ProxyLocationWebService() {
		try {
			this.endpointAddress = PropertiesReader.getendpointAddress();
			
			//this.kpiMeasure = ProxyLocationKpiApplication.getInstance();
			kpiMeasure.setModuleVersion(PropertiesReader.getModuleVersion());
			kpiMeasure.setMODULE_NAME(PropertiesReader.getModuleName());
			logger.info("Endpoint successfully read from properties");
		} catch (IOException e) {
			logger.error("Error reading endpoint from properties");
			logger.error(e.getMessage());
		}
	}

	public void startServer() {

		proxyLocationImpl = new ProxyLocationImpl();
		proxyLocationImpl.setKpiMeasure(this.kpiMeasure);
		try {
			endpoint = javax.xml.ws.Endpoint.create(proxyLocationImpl);
			endpoint = javax.xml.ws.Endpoint.publish(this.endpointAddress, proxyLocationImpl);
			logger.info("ProxyLocation Server Start listening on: "+this.endpointAddress);
		} catch (Exception e) {
			logger.error("Error starting Webserver");
			logger.error(e.getMessage());
		}
	}
}
