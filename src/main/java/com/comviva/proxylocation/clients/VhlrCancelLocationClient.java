package com.comviva.proxylocation.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.comviva.proxylocation.kpi.KPIS;
import com.mahindracomviva.cms.common.monitoring.MarkType;
import com.mahindracomviva.cms.common.monitoring.MeasureUtil;

public class VhlrCancelLocationClient implements Runnable{

	private String[] endpoints;
	private Integer retryDelay;
	private Integer retries;
	private Integer i; // variable to check endpoints reference
	private String imsi;
	private String response;
	private VhlrClient vhlrClient;
	private String errorCode = "201";
	private String resultDescription = "";
	Logger logger = Logger.getLogger(VhlrCancelLocationClient.class);

	public VhlrCancelLocationClient(String endpointAddress, String imsi, String retries, String timeout, VhlrClient vhlrClient) {

		this.endpoints = endpointAddress.split(";");
		this.i = 0;
		this.vhlrClient = vhlrClient;
		this.imsi = imsi;
		this.retryDelay = Integer.parseInt(timeout);
		this.retries = Integer.parseInt(retries);
		
	}

	private Integer sentRequest(String endpoint) {
		//endpoint = "http://localhost:8081/VhlrSrvr?wsdl"; 
		Integer status = 404;

		try {
			// URL url = new URL("http://localhost:8088/InstantLink?wsdl");
			URL url = new URL(endpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(retryDelay);
			connection.setReadTimeout(retryDelay);
			byte[] buffer = createRequest().getBytes();

			connection.setRequestProperty("Content-Length", String.valueOf(buffer.length));
			connection.setRequestProperty("Content-Type", "application/soap+xml; charset=" + "UTF-8");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.connect();
			
			this.response = httpRead(connection, buffer);
			
			this.errorCode = getStatus(this.response,"errorCode");
			status = Integer.parseInt(this.errorCode);
			
			logger.info("Response for unregisterImsi received");
			logger.info(this.response);
			
		} catch (Exception e) {
			logger.error("Error on sentCancelRequestThread");
			this.resultDescription = e.getMessage()+" with VHLRServer";
			logger.error(resultDescription+" with VHLRServer");
		}
		return status;
	}

	private String getStatus(String response, String key) {
		String status = "404";

		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response)));

			NodeList errNodes = doc.getElementsByTagName(key);
		
			if (errNodes.getLength()>0) 
				status = errNodes.item(0).getTextContent();
			else status = "-";

		} catch (Exception e) {
			logger.error("Error on getCancelStatusThread");
			this.resultDescription = e.getMessage()+" with VHLRServer";
			logger.error(resultDescription+" with VHLRServer");
		}
		return status;
	}

	private String httpRead(HttpURLConnection connection, byte[] requestSoap) throws IOException {
	
		java.io.OutputStream out = null;
		out = connection.getOutputStream();
		BufferedReader in = null;
		String response = "404";

		try {
			out.write(requestSoap);
			out.close();
			// System.out.println("[BUILD] - READ RESPONSE");
			int responseCode = connection.getResponseCode();
			// System.out.println("[BUILD] - Comprobar -");
			String responseString = "";
			String outputString = "";
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
				in = new BufferedReader(isr);
				while ((responseString = in.readLine()) != null) {
					outputString += responseString;
				}
				response = outputString;
			} else {
				// System.out.println("[BUILD] - httpRead -MAL-");
				InputStreamReader isr = new InputStreamReader(connection.getErrorStream(), "UTF-8");
				// System.out.println("[BUILD] - httpRead -MAL2-");
				in = new BufferedReader(isr);
				// System.out.println("[BUILD] - httpRead -MAL3-");
				while ((responseString = in.readLine()) != null) {
					outputString += responseString;
				}
				response = outputString;

			} // if-else
		} finally {
			try {
				if (in != null) {
					logger.info("Connection close");
					in.close();
				}
			} catch (Exception e) {
				logger.error("Error clossing connection");
				logger.error(e.getMessage());
			}
		}
		return response;

	}// httpRead

	private String createRequest() {
		
		String request = 	"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vsr=\"http://tempuri.org/vsrvr.xsd\">\n"
						+	"<soapenv:Header/>\n"
						+		"<soapenv:Body>\n"
						+			"<vsr:imsi>"+this.imsi+"</vsr:imsi>\n"
						+		"</soapenv:Body>\n"
						+	"</soapenv:Envelope>";
		
		logger.info("Sending unregisterImsi request");
		logger.info(request.replace("\n", ""));
		
		return request;
	}

	@Override
	public void run() {
		MeasureUtil.measureWithException(KPIS.cancelLocation.getName(), measure ->{
			Integer status = 404;
			Integer index = 0;
	
			try {
				status = sentRequest(this.endpoints[index]);
			} catch (Exception e) {
				logger.error("Error on run first request");
				this.resultDescription = e.getMessage()+" with VHLRServer";
				logger.error(resultDescription+" with VHLRServer");
				measure.mark(MarkType.ERROR);
			}
			this.retries--;
	
			// MANEJO DE REINTENTOS
			while (status != 0 && retries > 0) { 	// Status==1 only temporary error
				try {
					Thread.sleep(this.retryDelay);
				} catch (InterruptedException e) {
					logger.error("Error on delay request");
					logger.error(e.getMessage());
					measure.mark(MarkType.ERROR);
				}
	
				if (index == endpoints.length - 1) { // seteo variable indice Endpoints Array
					index = 0; // si no hay mas opciones reintento desde la primera
				} else {
					index++;
				}
	
				try {
					logger.info("Trying cancelrequest with retry");
					status = sentRequest(this.endpoints[index]);
					
				} catch (Exception e) {
					logger.error("Error sending request on method run");
					this.resultDescription = e.getMessage()+" with VHLRServer";
					logger.error(resultDescription+" with VHLRServer");
					measure.mark(MarkType.ERROR);
				}
				retries--;
			}
			
			if (status == 0) {
				sendOkResponse();
			} else {
				sendErrorResponse();
				measure.mark(MarkType.ERROR);
			}
		});
	}//end of run
	
	private void sendOkResponse() {
		
		String rspCode = getStatus(this.response,"rspCode");
		
		this.vhlrClient.setCancelResponse(this.errorCode, rspCode.equals("") ? "-" : rspCode);
		
		logger.info("Response for unregisterImsi succesfully load");
	}
	
	private void sendErrorResponse() {

		this.vhlrClient.setResponse(this.errorCode, this.resultDescription, "-", "-", "-", "-", "-","-");
		logger.info("Response from cancelLocation with error succefully load");
	}
	
}