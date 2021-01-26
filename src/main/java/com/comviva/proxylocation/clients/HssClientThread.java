package com.comviva.proxylocation.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.comviva.proxylocation.kpi.KPIS;
import com.mahindracomviva.cms.common.monitoring.MarkType;
import com.mahindracomviva.cms.common.monitoring.MeasureUtil;

public class HssClientThread  implements Runnable{

	private String[] endpoints;
	private Integer retryDelay;
	private Integer retries;
	private Integer i; // variable to check endpoints reference
	private String imsi;
	private String hssAddress;
	private String response;
	private VhlrClient vhlrClient;
	private String errorCode = "201";
	private String resultDescription = "";
	private String net;
	Logger logger = Logger.getLogger(HssClientThread.class);

	public HssClientThread(String endpointAddress, String imsi, String hssAddress, String retries, String timeout, VhlrClient vhlrClient, String net) {

		this.endpoints = endpointAddress.split(";");
		this.i = 0;
		this.vhlrClient = vhlrClient;
		this.imsi = imsi;
		this.hssAddress = hssAddress;
		this.retryDelay = Integer.parseInt(timeout);
		this.retries = Integer.parseInt(retries);
		this.net = net;
		
	}

	private Integer sentRequest(String endpoint) {
		//endpoint = "http://localhost:8081/VhlrSrvr?wsdl"; 
		Integer status = 404;

		try {
			
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
			
			logger.info("Response received");
			logger.info(this.response);
			
		} catch (Exception e) {
			logger.error("Error on sentRequestThread");
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
			logger.error("Error on getStatusThread");
			this.resultDescription = e.getMessage()+" with VHLRServer";
			logger.error(resultDescription+" with VHLRServer");
		}
		return status;
	}
	
	private String[] getTrackingAreaIdentityResponse (String response) {
		String [] trackingValues = new String[4];
		
		trackingValues[0] = "RawData";
		trackingValues[1] = "MobileCountryCode";
		trackingValues[2] = "MobileNetworkCode";
		trackingValues[3] = "TrackingAreaCode";

		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response)));
			
			NodeList nodeValues;
			
			for (int i = 0 ; i<trackingValues.length ; i++) {
				
				nodeValues = doc.getElementsByTagName(trackingValues[i]);
				
				if (nodeValues.getLength()>0)
					if (nodeValues.getLength()>1)
						trackingValues[i] = nodeValues.item(1).getTextContent();
					else trackingValues[i] = nodeValues.item(0).getTextContent();
				else trackingValues[i] = "-";
				
			}

		} catch (Exception e) {
			logger.error("Error on getStatusThread");
			this.resultDescription = e.getMessage()+" with VHLRServer";
			logger.error(resultDescription+" with VHLRServer");
		}
		return trackingValues;
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
		   +				"<soapenv:Header/>\n"
		   +				"<soapenv:Body>\n"
		   +   					"<vsr:lteInsertSubscriberDataReq>\n"
		   +      					"<IMSI>"+this.imsi+"</IMSI>\n"
		   +      					"<MMEIdentity>"+this.hssAddress+"</MMEIdentity>\n"
		   +				   	"</vsr:lteInsertSubscriberDataReq>\n"
		   +				"</soapenv:Body>\n"
		   +				"</soapenv:Envelope>";

		logger.info("Sending request");
		logger.info(request.replace("\n", ""));
		
		return request;
	}

	@Override
	public void run() {
		MeasureUtil.measureWithException(KPIS.proxyLocation.getName(), measure ->{
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
			while (status != 0 && status != 253 && retries > 0) { 	// Status==1 only temporary error
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
					logger.info("Trying request with retry");
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
		
		String [] trackingAreaIdentityResponse = getTrackingAreaIdentityResponse(this.response);
		
		String rspCode = getStatus(this.response,"rspCode");
		String rawData = trackingAreaIdentityResponse[0];
		String mcc = trackingAreaIdentityResponse[1];
		String mnc = trackingAreaIdentityResponse[2];
		String lac = trackingAreaIdentityResponse[3];
		String cgi = "-";
		
		String area = lac.substring(1, 3);
		lac= lac.replace(area, "");
		lac = lac+area;
		
		lac = Integer.toHexString(Integer.valueOf(lac)).toUpperCase();
		
		this.vhlrClient.setResponse(this.errorCode, rspCode.equals("") ? "-" : rspCode, rawData.equals("") ? "-" : rawData, mcc.equals("") ? "-" : mcc, mnc.equals("") ? "-" : mnc, lac.equals("") ? "-" : lac, cgi.equals("") ? "-" : cgi, this.net);
		
		logger.info("Response succesfully load");
	}
	
	private void sendErrorResponse() {

		this.vhlrClient.setResponse(this.errorCode, this.resultDescription, "-", "-", "-", "-", "-","-");
		logger.info("Response with error succefully load");
	}
	
}