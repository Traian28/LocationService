package com.comviva.proxylocation.clients;

import java.io.IOException;
import org.apache.log4j.Logger;

import com.comviva.proxylocation.kpi.KPIS;
import com.comviva.proxylocation.kpi.ProxyLocationKpiApplication;
import com.comviva.proxylocation.schema.ProxyCancelLocationResponse;
import com.comviva.proxylocation.schema.ProxyLocationResponse;
import com.comviva.proxylocation.utils.PropertiesReader;
import com.mahindracomviva.cms.common.monitoring.MeasureUtil;

public class VhlrClient {

	private ProxyLocationResponse response;
	private ProxyCancelLocationResponse cancelResponse;
	private LocationInformation locationInformation;
	private String imsi;
	private String origin;
	Logger logger = Logger.getLogger(VhlrClient.class);

	/** Medicion de Kpi*/
	private ProxyLocationKpiApplication kpiMeasure;
	
	public VhlrClient(String imsi, String origin) {
		
		locationInformation = new LocationInformation(imsi, origin);
		response = new ProxyLocationResponse();
		cancelResponse = new ProxyCancelLocationResponse();
		this.imsi = imsi;
		this.origin = origin;
		
	}
	
	public ProxyLocationKpiApplication getKpiMeasure() {
		return kpiMeasure;
	}


	public void setKpiMeasure(ProxyLocationKpiApplication kpiMeasure) {
		this.kpiMeasure = kpiMeasure;
	}

	public ProxyLocationResponse sendRequest() {
		return MeasureUtil.measureWithReturnAndException(KPIS.dbStatus.getName(), measureDB ->{
			logger.info("Generating client to VHLR endpoint");
			
			setDefaultResponse();
			
			String endpoint = locationInformation.getEndpoint();
			if (!endpoint.isEmpty()) {
				logger.info("Endpoint get from database for vhlr: "+endpoint);
				String [] connProperties = new String[2];
				try {
				//getProperties
				connProperties = PropertiesReader.getCustomProperties("proxy.connection.retries","proxy.connection.timeout");
				} catch (IOException e) {
					logger.error("Error reading connection properties");
					logger.error(e.getMessage());
				}
				
				if (hasAddress(locationInformation.getAddress())) {
				
					if (origin.contentEquals("hlr") || origin.contentEquals("HLR")) {
						VhlrClientThread vhlrThread = new VhlrClientThread(endpoint, this.imsi, locationInformation.getAddress(), connProperties[0], connProperties[1], this, locationInformation.getNet());
						vhlrThread.run();
							
					} else if (origin.contentEquals("hss") || origin.contentEquals("HSS")) {
							HssClientThread hssThread = new HssClientThread(endpoint, this.imsi, locationInformation.getAddress(), connProperties[0], connProperties[1], this, locationInformation.getNet());
							hssThread.run();
					}
				} else {
					
					logger.error("No address found for imsi= "+imsi+" and origin= "+origin);
					setResponse ("9", "No address found in database", "-", "-", "-", "-", "-", "-"); 
					
				}
				
			}
			return this.response;
		});
	}
	
	public ProxyCancelLocationResponse sendCancelRequest() { //operation for cancelLocation
		return MeasureUtil.measureWithReturnAndException(KPIS.dbStatus.getName(), measureDB ->{
			logger.info("Generating client to VHLR cancelEndpoint");
			
			String endpoint = locationInformation.getEndpoint();
			
			setDefaultCancelResponse();
			
			if (!endpoint.isEmpty()) {
				logger.info("Endpoint get from database for vhlr: "+endpoint);
				String [] connProperties = new String[2];
				try {
				//getProperties
				connProperties = PropertiesReader.getCustomProperties("proxy.connection.retries","proxy.connection.timeout");
				} catch (IOException e) {
					logger.error("Error reading connection properties");
					logger.error(e.getMessage());
				}
				
				VhlrCancelLocationClient vhlrCancelThread1 = new VhlrCancelLocationClient(endpoint, this.imsi, connProperties[0], connProperties[1], this);
				vhlrCancelThread1.run();
				
				if (locationInformation.getNet().equals("hlrhss")) {
					endpoint = locationInformation.getSecondaryEndpoint();
					logger.info("Endpoint get from database for cancelLocation: "+endpoint);
					VhlrCancelLocationClient vhlrCancelThread2 = new VhlrCancelLocationClient(endpoint, this.imsi, connProperties[0], connProperties[1], this);
					vhlrCancelThread2.run();
				}
			}
			
			return this.cancelResponse;
		});
	}
	
	private boolean hasAddress (String address) {
		
		return (address!=null && !address.contentEquals(""));
		
	}

	public void setResponse (String resultCode, String resultDesc, String rawData, String mcc, String mnc, String lac, String cgi, String origin) {
		
		logger.info("Response: resultCode="+ resultCode+" resultDescription="+resultDesc+" rawData="+rawData+" mcc="+ mcc+" mnc="+mnc+" lac="+lac+" cgi="+cgi+" origin="+origin);
		
		this.response.setResultCode(resultCode);
		this.response.setResultDescription(resultDesc);
		this.response.setRawData(rawData);
		this.response.setMobileCountryCode(mcc);
		this.response.setMobileNetworkCode(mnc);
		this.response.setLocationAreaCode(lac);
		this.response.setCellIdentityOrSAI(cgi);
		this.response.setNet(origin);

	}
	
	public void setCancelResponse (String resultCode, String resultDesc) {
		
		if (cancelResponse.getResultCode().equals("0")) {
			this.cancelResponse.setResultCode(resultCode);
			this.cancelResponse.setResultDescription(resultDesc);
		} else if (cancelResponse.getResultCode().equals("404")) {
				this.cancelResponse.setResultCode(resultCode);
				this.cancelResponse.setResultDescription(resultDesc);
		}
	}
	
	private void setDefaultResponse () {
		
		this.response.setResultCode("201");
		this.response.setResultDescription("Error getting response from VHLR ws for GetLocationOperation");
		this.response.setCellIdentityOrSAI("");
		this.response.setLocationAreaCode("");
		this.response.setMobileCountryCode("");
		this.response.setMobileNetworkCode("");
		this.response.setRawData("");
		this.response.setNet("");
		
	}
	
	private void setDefaultCancelResponse () {
		
		this.cancelResponse.setResultCode("404");
		this.cancelResponse.setResultDescription("Error getting response from VHLR ws for CancelOperation");
	
	}

}