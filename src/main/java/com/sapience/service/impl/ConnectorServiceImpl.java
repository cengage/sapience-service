package com.sapience.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.sapience.connector.ConnectorFetchAndSaveBO;
import com.sapience.dao.ConnectorDao;
import com.sapience.service.ConnectorService;

public class ConnectorServiceImpl implements ConnectorService {

	private ConnectorFetchAndSaveBO connectorFetchAndSaveBO;

	private ConnectorDao connectorDao;

	@Inject
	public void setConnectorFetchAndSaveBO(
			ConnectorFetchAndSaveBO connectorFetchAndSaveBO) {
		this.connectorFetchAndSaveBO = connectorFetchAndSaveBO;
	}

	@Inject
	public void setConnectorDao(ConnectorDao connectorDao) {
		this.connectorDao = connectorDao;
	}

	public List<String> fetchAndSaveJankinData() {
		return connectorFetchAndSaveBO.fetchAndSaveJankinData();

	}

	public List<String> fetchAndSaveJiraData() {
		return connectorFetchAndSaveBO.fetchAndSaveJiraData();

	}

	@Override
	public String saveAllFetchedData(Map<String, String> fetchedData) {
		return connectorDao.saveAllFetchedData(fetchedData);

	}

}