package com.sapience.web;

import javax.inject.Inject;

import com.sapience.service.ConnectorService;

public class MyApplication {

	private ConnectorService connectorService;

	@Inject
	public void setConnectorService(ConnectorService connectorService) {
		this.connectorService = connectorService;
	}

	public String fetchAndSaveJankinData() {
		return connectorService.fetchAndSaveJankinData();
	}

	public String fetchAndSaveJiraData() {
		return connectorService.fetchAndSaveJiraData();
	}

}