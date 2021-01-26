package com.comviva.proxylocation.test;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;

public class MainTest {

//	public static void main (String[] args) {
//	
//		
//		String decimal = "32183";
//		
//		String area = decimal.substring(1, 3);
//		decimal= decimal.replace(area, "");
//		decimal = decimal+area;
//		System.out.println(decimal);
//		String hexa = Integer.toHexString(Integer.valueOf(decimal)).toUpperCase();
//		
//		System.out.println(hexa);
		
//		String currentTimeString;
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		
//		currentTimeString = format.format(new Date());
//		
//		System.out.println (currentTimeString);
//		
//		String origin = "hl";
//		//System.out.println (origin.substring(0,3));
//		if (origin.length()>=3) {
//			
//			if ( (origin.substring(0,3)).contentEquals("hlr") || (origin.substring(0,3)).contentEquals("HLR") || (origin.substring(0,3)).contentEquals("hss") || (origin.substring(0,3)).contentEquals("HSS") )
//		
//				System.out.println ("OK");
//				
//				else System.out.println("NOK");
//		
//		}else System.out.println ("lengtBad");
//		
//	}


	
}
	


	
//		// TODO Auto-generated method stub

//	public static void main (String[] args) {
//		Integer status = 404;
//			
//			String request = 	"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vsr=\"http://tempuri.org/vsrvr.xsd\">\n"
//					   +				"<soapenv:Header/>\n"
//					   +				"<soapenv:Body>\n"
//					   +   					"<vsr:lteInsertSubscriberDataReq>\n"
//					   +      					"<IMSI>724031799504047</IMSI>\n"
//					   +      					"<MMEIdentity>552182740000</MMEIdentity>\n"
//					   +				   	"</vsr:lteInsertSubscriberDataReq>\n"
//					   +				"</soapenv:Body>\n"
//					   +				"</soapenv:Envelope>";
//			
//			try {
//				status = sentRequest("http://localhost:8080/VhlrSrvr?WSDL", request);
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//			
//			System.out.println(status);
//
//		
//	}
//	
//	private static Integer sentRequest(String endpoint, String request) {
//		Integer status = 404;
//
//		try {
//			// URL url = new URL("http://localhost:8088/InstantLink?wsdl");
//			URL url = new URL(endpoint);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setConnectTimeout(2000);
//			byte[] buffer = request.getBytes();
//
//			connection.setRequestProperty("Content-Length", String.valueOf(buffer.length));
//			connection.setRequestProperty("Content-Type", "application/soap+xml; charset=" + "UTF-8");
//			connection.setRequestMethod("POST");
//			connection.setDoOutput(true);
//			connection.setDoInput(true);
//
//			connection.connect();
//			
//			String response = httpRead(connection, buffer);
//			
//			status = Integer.parseInt(getStatus(response,"errorCode"));
//			
//			System.out.println("Response received");
//			
//			System.out.println(response);
//			
////			System.out.println("1"+getStatus(response,"rspCode"));
////			System.out.println("1"+getStatus(response,"RawData"));
////			System.out.println("1"+getStatus(response,"MobileCountryCode"));
////			System.out.println("1"+getStatus(response,"MobileNetworkCode"));
////			System.out.println("1"+getStatus(response,"LocationAreaCode"));
////			System.out.println("1"+getStatus(response,"CellIdentityOrSAI"));
//			
//			String [] trackingAreaIdentityResponse = getTrackingAreaIdentityResponse(response);
//			
//			System.out.println (getStatus(response,"LocationAreaCode"));
//			System.out.println (getStatus(response,"CellIdentityOrSAI"));
//			
//			for (int i = 0; i<trackingAreaIdentityResponse.length;i++)
//				System.out.println(trackingAreaIdentityResponse[i]);
//			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return status;
//	}
//	
//	private static String httpRead(HttpURLConnection connection, byte[] requestSoap) throws IOException {
//		
//		java.io.OutputStream out = null;
//		out = connection.getOutputStream();
//		BufferedReader in = null;
//		String response = "404";
//
//		// System.out.println("[BUILD] - Envio SOAP REQUEST");
//		try {
//			out.write(requestSoap);
//			out.close();
//			// System.out.println("[BUILD] - READ RESPONSE");
//			int responseCode = connection.getResponseCode();
//			// System.out.println("[BUILD] - Comprobar -");
//			String responseString = "";
//			String outputString = "";
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
//				in = new BufferedReader(isr);
//				while ((responseString = in.readLine()) != null) {
//					outputString += responseString;
//				}
//				response = outputString;
//			} else {
//				// System.out.println("[BUILD] - httpRead -MAL-");
//				InputStreamReader isr = new InputStreamReader(connection.getErrorStream(), "UTF-8");
//				// System.out.println("[BUILD] - httpRead -MAL2-");
//				in = new BufferedReader(isr);
//				// System.out.println("[BUILD] - httpRead -MAL3-");
//				while ((responseString = in.readLine()) != null) {
//					outputString += responseString;
//				}
//				response = outputString;
//
//			} // if-else
//		} finally {
//			try {
//				if (in != null) {
//					System.out.println("close");
//					in.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return response;
//
//	}// httpRead
//	
//	private static String getStatus(String response, String key) {
//		String status = "404";
//
//		Document doc = null;
//		try {
//			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
//					.parse(new InputSource(new StringReader(response)));
//
//			NodeList errNodes = doc.getElementsByTagName(key);
//			
//			if (errNodes.getLength()>0) 
//				status = errNodes.item(0).getTextContent();
//			else status = "-";
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return status;
//	}
//	
//	private static String[] getTrackingAreaIdentityResponse (String response) {
//		String [] trackingValues = new String[4];
//		
//		trackingValues[0] = "RawData";
//		trackingValues[1] = "MobileCountryCode";
//		trackingValues[2] = "MobileNetworkCode";
//		trackingValues[3] = "TrackingAreaCode";
//
//		Document doc = null;
//		try {
//			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
//					.parse(new InputSource(new StringReader(response)));
//			
//			NodeList nodeValues;
//			
//			for (int i = 0 ; i<trackingValues.length ; i++) {
//				
//				nodeValues = doc.getElementsByTagName(trackingValues[i]);
//				
//				if (nodeValues.getLength()>0)
//					if (nodeValues.getLength()>1)
//						trackingValues[i] = nodeValues.item(1).getTextContent();
//					else trackingValues[i] = nodeValues.item(0).getTextContent();
//				else trackingValues[i] = "-";
//				
//			}
//
////
////            XPathFactory xPathfactory = XPathFactory.newInstance();
////            XPath xpath = xPathfactory.newXPath();
////            XPathExpression expression = xpath.compile("/Service/Inquiry/InquiryType/text()");
////            NodeList xpathNodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
////            System.out.println("InquiryType is : " +xpathNodeList.item(0));
//			
//			//Element element = doc.getElementById("TrackingAreaIdentity");
//			//System.out.println(doc.getgetLength());
//			//NodeList nodes = element.getgetChildNodes();
//			//System.out.println(element.getElementsByTagName("TrackingAreaIdentity").getLength());
//			//System.out.println("leng"+nodes.getLength());
////			for (int i = 0; i < nodes.getLength(); ++i) {
////				node = nodes.item(i);
////				System.out.println(node.getNodeName());
////				System.out.println(node.getNodeValue());
////			    if ( nodes.item(i).getNodeName().equalsIgnoreCase(trackingValues[0]) )
////			    	trackingValues[0] = nodes.item(i).getTextContent();
////			    else if ( nodes.item(i).getNodeName().equalsIgnoreCase(trackingValues[1]) )
////			    		trackingValues[1] = nodes.item(i).getTextContent();
////			    	else if ( nodes.item(i).getNodeName().equalsIgnoreCase(trackingValues[2]) )
////			    			trackingValues[2] = nodes.item(i).getTextContent();
////			    		else if ( nodes.item(i).getNodeName().equalsIgnoreCase(trackingValues[3]) )
////			    				trackingValues[3] = nodes.item(i).getTextContent();
////
////			}
//		} catch (Exception e) {
//			System.out.println("Error on getStatusThread");
//			//this.resultDescription = e.getMessage()+" with VHLRServer";
//			System.out.println(e.getMessage());
//		}
//
//		return trackingValues;
//	}
//}
//
