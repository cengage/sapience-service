package com.sapience.service;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;
import com.sapience.service.impl.ConnectorServiceImpl;

@ImplementedBy(ConnectorServiceImpl.class)
public interface ConnectorService {

	List<String> fetchAndSaveJankinData();

	List<String> fetchAndSaveJiraData();

	String saveAllFetchedData(Map<String, String> fetchedData);

}