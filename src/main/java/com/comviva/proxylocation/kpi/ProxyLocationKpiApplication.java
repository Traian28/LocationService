package com.comviva.proxylocation.kpi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mahindracomviva.cms.common.BaseApplication;
import com.mahindracomviva.cms.common.monitoring.CapacityMeasure;
import com.mahindracomviva.cms.common.monitoring.GenericMeasure;
import com.mahindracomviva.cms.common.monitoring.KpiGeneralStatus;
import com.mahindracomviva.cms.common.monitoring.KpiStatus;
import com.mahindracomviva.cms.common.monitoring.MeasureUtil;
import com.mahindracomviva.cms.common.monitoring.MetaMeasure;
import com.mahindracomviva.cms.common.monitoring.ModuleError;
import com.mahindracomviva.cms.common.monitoring.ModuleStatus;
import com.mahindracomviva.cms.common.monitoring.Status;
import com.mahindracomviva.cms.common.monitoring.TrafficMeasure;
import com.mahindracomviva.cms.common.monitoring.Status.Code;

public class ProxyLocationKpiApplication extends BaseApplication {
	// Constant KPI
	private String MODULE_NAME 		= "ProxyLocation";
	private String moduleVersion 					= "1.1.0";
	
	private static ProxyLocationKpiApplication instance = new ProxyLocationKpiApplication();
	
	private String currentTimeString;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	List<ModuleError> moduleErrorList;
	
	public ProxyLocationKpiApplication() {
		super(true);
	}

	public static ProxyLocationKpiApplication getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return MODULE_NAME;
	}

	public void setMODULE_NAME(String mODULE_NAME) {
		MODULE_NAME = mODULE_NAME;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	public void setModuleVersion(String moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	@Override
	public String getVersion() {
		return getModuleVersion();
	}
	
	@Override
	public ModuleStatus getModuleStatus() {
		final Set<CapacityMeasure> capacity  = new LinkedHashSet<>();
		final Set<MetaMeasure> meta = new LinkedHashSet<>();
		final Set<TrafficMeasure> traffic = new LinkedHashSet<>();
		final Set<GenericMeasure> generic = new LinkedHashSet<>();
		
		String maxCapacity = "500";//environment.getProperty("kpi.max.capacity");
		
		MeasureUtil.resetAccumulators(maxCapacity, capacity, meta, traffic, generic);
		
		//Cuenta errores especificos para sumarizar
		analizeStatus(capacity, meta, traffic, generic);
		
		//Determina el estado general
		KpiGeneralStatus gralStatus = MeasureUtil.getStatusFromKpi(capacity, meta, traffic, generic);

		//Construye los errores del modulo a partir del status general
		buildModuleErrors(gralStatus);
			
		//Saca el estado general para informar
		KpiStatus kpiStatus = gralStatus.getGeneralStatus();
			
		Status status = new Status(MODULE_NAME, getVersion(), setCodeStatus(kpiStatus), kpiStatus.getDescription());
			
		fillEmptyKPIs(capacity, generic);
		
		return new ModuleStatus(status, gralStatus, moduleErrorList, generic, traffic, capacity, meta);

	}
	
	private void analizeStatus(Set<CapacityMeasure> capacity, Set<MetaMeasure> meta, Set<TrafficMeasure> traffic, Set<GenericMeasure> generic) {
		
		int erroCount = 0, keyElemError = -1;
				
		//Contar los errores y sumarlos a Error
		Iterator itGe = generic.iterator();
		GenericMeasure genMe;
		int cantGenMe = 0;
		while (itGe.hasNext()) {
			genMe = (GenericMeasure)itGe.next();
			if (genMe.getRowId().contains("102.Count") || genMe.getRowId().contains("101.Count")) {
				erroCount = erroCount + Integer.valueOf(genMe.getValue());
				currentTimeString = format.format(new Date());
				//System.out.println(currentTimeString + " Cant Error total es: "+erroCount);
			} else if (genMe.getRowId().contains("ERROR.Count")) 
				keyElemError = cantGenMe;
			cantGenMe++;
		}
		
		itGe = generic.iterator();
		boolean fin = false;
		while (itGe.hasNext() && !fin) {
			genMe = (GenericMeasure)itGe.next();
			if (genMe.getRowId().contains("ERROR.Count")) { 
				int before = (genMe.getValue() != null) ? Integer.valueOf(genMe.getValue()) : 0;
				genMe.setValue(String.valueOf(erroCount + before));
				//Update measure ERROR.Count
				generic.remove(Integer.valueOf(keyElemError));
				generic.add(genMe);
				fin = true;
				//System.out.println(currentTimeString + " Cant Error total es: "+genMe.getValue());
			}
		}
		
	}
	
	private Code setCodeStatus(KpiStatus kpi) {
		return (kpi.getStatus().equalsIgnoreCase("OK") ? Code.OK : 
		(kpi.getStatus().equalsIgnoreCase("WARNING") ? Code.WARNING : Code.ERROR));
		
	}
	
	private void buildModuleErrors(KpiGeneralStatus gralStatus) {
		moduleErrorList = new ArrayList<>();
		gralStatus.getListStatus().forEach((key, value) ->
			moduleErrorList.add(new ModuleError(value.getStatus(), value.getName(), value.getDescription()))
		);
	}

	private void fillEmptyKPIs( Set<CapacityMeasure> capacity, Set<GenericMeasure> generic ) {
	    
	    for (final KPIS kpiEnum : KPIS.values())
	    {
	      final String kpi = kpiEnum.getName();
	      if (kpi.contains(KPIS.proxyLocation.getName()) || kpi.contains(KPIS.cancelLocation.getName())) {
		      addIfNotContainGeneric(generic, kpi + ".Total.Count");
		      addIfNotContainGeneric(generic, kpi + ".Total.AvgTime");
		      addIfNotContainGeneric(generic, kpi + ".Total.MinTime");
		      addIfNotContainGeneric(generic, kpi + ".Total.MaxTime");
		      addIfNotContainGeneric(generic, kpi + ".OK.Count");
		      addIfNotContainGeneric(generic, kpi + ".OK.AvgTime");
		      addIfNotContainGeneric(generic, kpi + ".OK.MaxTime");
		      addIfNotContainGeneric(generic, kpi + ".OK.MinTime");
		      addIfNotContainGeneric(generic, kpi + ".ERROR.Count");
		      addIfNotContainGeneric(generic, kpi + ".ERROR.AvgTime");
		      addIfNotContainGeneric(generic, kpi + ".ERROR.MaxTime");
		      addIfNotContainGeneric(generic, kpi + ".ERROR.MinTime");
		      addIfNotContainGeneric(generic, kpi + ".TimeOut.Count");
		      addIfNotContainGeneric(generic, kpi + ".TimeOut.AvgTime");
		      addIfNotContainGeneric(generic, kpi + ".TimeOut.MaxTime");
		      addIfNotContainGeneric(generic, kpi + ".TimeOut.MinTime");	    	  
	      }
	    }
	}
	
	  private void addIfNotContainGeneric( Set<GenericMeasure> generic, String kpiName )
	  {
	    String unit = (kpiName.endsWith("Count") || kpiName.endsWith("Error.Count") || kpiName.endsWith("OK.Count")) ? "qty" : "ms";
	    if (!generic.contains(kpiName))
	    {
	      generic.add(new GenericMeasure(kpiName, "0", unit));
	    }
	  }
}
