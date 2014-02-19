package com.sapience.service;

import java.util.Map;

import com.google.inject.ImplementedBy;
import com.sapience.service.impl.ConnectorServiceImpl;

@ImplementedBy(ConnectorServiceImpl.class)
public interface ConnectorService {

	String fetchAndSaveJankinData();

	String fetchAndSaveJiraData();

	String saveAllFetchedData(Map<String, String> fetchedData);

}