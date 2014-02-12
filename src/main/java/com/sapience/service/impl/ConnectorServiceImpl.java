package com.sapience.service.impl;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.sapience.connector.ConnectorFetchAndSaveBO;
import com.sapience.service.ConnectorService;

public class ConnectorServiceImpl implements ConnectorService {

	private ConnectorFetchAndSaveBO connectorFetchAndSaveBO;

	@Inject
	public void setConnectorFetchAndSaveBO(
			ConnectorFetchAndSaveBO connectorFetchAndSaveBO) {
		this.connectorFetchAndSaveBO = connectorFetchAndSaveBO;
	}

	@Override
	public String fetchAndSaveJankinData() {
		return connectorFetchAndSaveBO.fetchAndSaveJankinData();

	}

	@Override
	public String fetchAndSaveJiraData() {
		return connectorFetchAndSaveBO.fetchAndSaveJiraData();

	}

}