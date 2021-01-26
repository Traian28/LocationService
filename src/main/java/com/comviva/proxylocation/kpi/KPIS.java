package com.comviva.proxylocation.kpi;

public enum KPIS {

	TPS("Tps"),
	proxyLocation("ProxyLocationRequest"),
	cancelLocation("ProxyCancelLocationRequest"),
	dbStatus("DB.Status");
	
	String name;

	private KPIS(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
