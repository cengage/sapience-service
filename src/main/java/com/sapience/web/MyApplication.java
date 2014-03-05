package com.sapience.web;

import java.util.List;

import javax.inject.Inject;

import com.sapience.service.ConnectorService;

public class MyApplication {

	private ConnectorService connectorService;

	@Inject
	public void setConnectorService(ConnectorService connectorService) {
		this.connectorService = connectorService;
	}

	public List<String> fetchAndSaveJankinData() {
		return connectorService.fetchAndSaveJankinData();
	}

	public List<String> fetchAndSaveJiraData() {
		return connectorService.fetchAndSaveJiraData();
	}

}