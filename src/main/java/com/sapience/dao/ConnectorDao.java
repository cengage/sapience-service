package com.sapience.dao;

import java.util.Map;

import com.google.inject.ImplementedBy;
import com.sapience.dao.impl.ConnectorDaoImpl;

@ImplementedBy(ConnectorDaoImpl.class)
public interface ConnectorDao {

	String saveAllFetchedData(Map<String, String> fetchedData);

}