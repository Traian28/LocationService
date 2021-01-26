package com.comviva.proxylocation.proxy;
import org.apache.log4j.Logger;

import com.comviva.proxylocation.clients.VhlrClient;
import com.comviva.proxylocation.kpi.KPIS;
import com.comviva.proxylocation.kpi.ProxyLocationKpiApplication;
import com.comviva.proxylocation.schema.ProxyCancelLocationRequest;
import com.comviva.proxylocation.schema.ProxyCancelLocationResponse;
import com.comviva.proxylocation.schema.ProxyLocationRequest;
import com.comviva.proxylocation.schema.ProxyLocationResponse;
import com.comviva.proxylocation.service.ProxyLocationServiceImpl;
import com.mahindracomviva.cms.common.monitoring.MarkType;
import com.mahindracomviva.cms.common.monitoring.MeasureUtil;

@javax.jws.WebService(serviceName = "proxyLocationServiceImplService", 
portName = "proxyLocationServiceImpl", 
targetNamespace = "http://service.proxyLocation.comviva.com", 
wsdlLocation = "wsdl/serviceLocation.wsdl", 
endpointInterface = "com.comviva.proxylocation.service.ProxyLocationServiceImpl")

public class ProxyLocationImpl implements ProxyLocationServiceImpl {
	Logger logger = Logger.getLogger(ProxyLocationImpl.class);

	/** Medicion de Kpi */
	private ProxyLocationKpiApplication kpiMeasure;

	public ProxyLocationKpiApplication getKpiMeasure() {
		return kpiMeasure;
	}

	public void setKpiMeasure(ProxyLocationKpiApplication kpiMeasure) {
		this.kpiMeasure = kpiMeasure;
		// this.vhlrClient.setKpiMeasure(kpiMeasure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.com.comviva.proxylocation.service.ProxyLocationServiceImpl#
	 * getLocation(com.comviva.proxylocation.schema.ProxyLocationRequest
	 * proxyLocationRequest)*
	 */
	public ProxyLocationResponse getLocation(ProxyLocationRequest proxyLocationRequest) {

		return MeasureUtil.measureWithReturnAndException(KPIS.proxyLocation.getName(), measure -> {
			logger.info("---------- Executing operation getLocation ----------");
			//logger.info(proxyLocationRequest.getoString());

			String origin = proxyLocationRequest.getOrigin();
			String imsi = proxyLocationRequest.getImsi();
			
			VhlrClient vhlrClient = new VhlrClient(imsi, origin.substring(0,3));
			vhlrClient.setKpiMeasure(kpiMeasure);
			
			com.comviva.proxylocation.schema.ProxyLocationResponse _return = new com.comviva.proxylocation.schema.ProxyLocationResponse();

			if (imsi != null && !imsi.equals("")) {

				if (origin != null && !origin.equals("")) {

					if (validOrigin (origin)) {
						
						logger.info("GetLocation for imsi: "+imsi+" Origin: "+origin);
					
							_return = vhlrClient.sendRequest();
							
							if (_return == null || !_return.getResultCode().equalsIgnoreCase("0")) {
								measure.mark(MarkType.ERROR);
								logger.info("Response sent with error during request");
							} else {
								measure.mark(MarkType.OK);
								logger.info("Response successfully sent");
							}
							return _return;
							
					} else { //origin not hlr nor hss
							logger.error("Request receive with wrong Origin: Error Origin not hlr nor hss");
							logger.info("Imsi: " + imsi);
							logger.info("Origin: " + origin);
	
							_return.setResultCode("9");
							_return.setResultDescription("Error Origin not hlr nor hss");
							_return.setNet("");
							_return.setRawData("");
							_return.setMobileCountryCode("");
							_return.setMobileNetworkCode("");
							_return.setLocationAreaCode("");
							_return.setCellIdentityOrSAI("");
	
							measure.mark(MarkType.ERROR);
							return _return;
					}
						
				} else { // origin is null or empty

					logger.error("Request receive with null origin");
					logger.info("Imsi: " + imsi);
					logger.info("Origin: " + origin);

					_return.setResultCode("9");
					_return.setResultDescription("Error Origin empty");
					_return.setNet("");
					_return.setRawData("");
					_return.setMobileCountryCode("");
					_return.setMobileNetworkCode("");
					_return.setLocationAreaCode("");
					_return.setCellIdentityOrSAI("");

					measure.mark(MarkType.ERROR);
					return _return;
				}

			} else { // means imsi is null or empty

				logger.error("Request receive with null Imsi");
				logger.info("Imsi: " + imsi);
				logger.info("Origin: " + origin);

				_return.setResultCode("9");
				_return.setResultDescription("Error ImsiNull");
				_return.setNet("");
				_return.setRawData("");
				_return.setMobileCountryCode("");
				_return.setMobileNetworkCode("");
				_return.setLocationAreaCode("");
				_return.setCellIdentityOrSAI("");

				measure.mark(MarkType.ERROR);
				return _return;
			}
		});
	}

	@Override
	public ProxyCancelLocationResponse cancelLocation(ProxyCancelLocationRequest proxyCancelLocationRequest) {
		return MeasureUtil.measureWithReturnAndException(KPIS.cancelLocation.getName(), measure -> {
			logger.info("---------- Executing operation cancelLocation ----------");

			String imsi = proxyCancelLocationRequest.getImsi();
			String origin = proxyCancelLocationRequest.getOrigin();
			
			VhlrClient vhlrClient = new VhlrClient(imsi, origin.substring(0,3));
			vhlrClient.setKpiMeasure(kpiMeasure);

			com.comviva.proxylocation.schema.ProxyCancelLocationResponse _return = new com.comviva.proxylocation.schema.ProxyCancelLocationResponse();

			if (imsi != null && !imsi.equals("")) {
				
				if (origin != null && !origin.equals("")) {	

					if (validOrigin (origin)) {

						logger.info("cancelLocation for imsi: "+imsi+" Origin: "+origin);
						_return = vhlrClient.sendCancelRequest();
					
						if (_return == null || !_return.getResultCode().equalsIgnoreCase("0"))
							measure.mark(MarkType.ERROR);
						else
							measure.mark(MarkType.OK);
						
						return _return;
					} else { //origin not hlr nor hss
						logger.error("Request receive with null origin");
						logger.info("Imsi: " + imsi);
						logger.info("Origin: " + origin);

						_return.setResultCode("9");
						_return.setResultDescription("Error Origin not hlr nor hss");

						measure.mark(MarkType.ERROR);
						return _return;
					}
				} else { // origin is null or empty

					logger.error("Request receive with null origin");
					logger.info("Imsi: " + imsi);
					logger.info("Origin: " + origin);
	
					_return.setResultCode("9");
					_return.setResultDescription("Error Origin empty");
	
					measure.mark(MarkType.ERROR);
					return _return;
				}
		} else { // means imsi is null or empty

			logger.error("Request receive with null imsi");
			logger.info("Imsi: " + imsi);
			logger.info("Origin: " + origin);

			_return.setResultCode("9");
			_return.setResultDescription("Error ImsiNull");

			measure.mark(MarkType.ERROR);
			return _return;
		}
	});

	}
	
	private boolean validOrigin (String origin) {
		
		if (origin.length()>=3)
			return ( (origin.substring(0,3)).contentEquals("hlr") || (origin.substring(0,3)).contentEquals("HLR") || (origin.substring(0,3)).contentEquals("hss") || (origin.substring(0,3)).contentEquals("HSS") );
			
		
		else return false;
	}

}