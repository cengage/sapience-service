package com.sapience.connector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.sapience.service.ConnectorService;

public class ConnectorFetchAndSaveBO {

	private ConnectorService connectorService;

	private JiraParseDataConnector jiraConnector;

	private JenkinsParseConnector jenkinParseconnector;

	@Inject
	public void setConnectorService(ConnectorService connectorService) {
		this.connectorService = connectorService;
	}

	@Inject
	public void setJiraConnector(JiraParseDataConnector jiraConnector) {
		this.jiraConnector = jiraConnector;
	}

	@Inject
	public void setJenkinParseconnector(
			JenkinsParseConnector jenkinParseconnector) {
		this.jenkinParseconnector = jenkinParseconnector;
	}

	public String fetchAndSaveJankinData() {
		String status = null;

		try {

			List<Map<String, String>> fetchedDataMapList = jenkinParseconnector
					.getValues();

			for (int i = 0; i < fetchedDataMapList.size(); i++) {

				Map<String, String> calculatedMap = new LinkedHashMap<String, String>();
				Map<String, String> fetchedData = new LinkedHashMap<String, String>();
				fetchedData = fetchedDataMapList.get(i);

				calculatedMap.put(fetchedData.keySet().toArray()[0].toString(),
						fetchedData.get(fetchedData.keySet().toArray()[0]));
				calculatedMap.put(fetchedData.keySet().toArray()[1].toString(),
						fetchedData.get(fetchedData.keySet().toArray()[1]));

				if (fetchedData.get(fetchedData.keySet().toArray()[1])
						.equalsIgnoreCase("TestAutomation")) {
					calculatedMap = calculateValueForTestAutoCategoryOrGICProject(
							fetchedData, calculatedMap);

				} else if ((fetchedData.get(fetchedData.keySet().toArray()[0])
						.equalsIgnoreCase("GIC") && fetchedData.get(
						fetchedData.keySet().toArray()[1]).equalsIgnoreCase(
						"Unit"))) {

					calculatedMap = calculateValueForTestAutoCategoryOrGICProject(
							fetchedData, calculatedMap);

				} else {

					calculatedMap = normalCalculateValueForJenkin(fetchedData,
							calculatedMap);

				}
				// ---- Save data in Database --------

				status = connectorService.saveAllFetchedData(calculatedMap);

				System.out.println("final status is :" + status);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	private Map<String, String> normalCalculateValueForJenkin(
			Map<String, String> fetchedData, Map<String, String> calculatedMap) {

		Double complexity = Double.parseDouble(fetchedData.get(fetchedData
				.keySet().toArray()[2]));
		Double cvrdElements = Double.parseDouble(fetchedData.get(fetchedData
				.keySet().toArray()[3]));
		Double totElements = Double.parseDouble(fetchedData.get(fetchedData
				.keySet().toArray()[4]));
		Double stmnts = Double.parseDouble(fetchedData.get(fetchedData.keySet()
				.toArray()[5]));
		Double mthds = Double.parseDouble(fetchedData.get(fetchedData.keySet()
				.toArray()[6]));

		String codeCvrg = String.valueOf((cvrdElements / totElements) * 100);
		String statementPerMethod = String.valueOf(stmnts / mthds);
		String cyclomaticComplexity = String.valueOf(complexity / mthds);

		calculatedMap.put("Code Coverage", codeCvrg);
		calculatedMap.put("Statements per Method", statementPerMethod);
		calculatedMap.put("Cyclomatic Complexity", cyclomaticComplexity);

		return calculatedMap;
	}

	private Map<String, String> calculateValueForTestAutoCategoryOrGICProject(
			Map<String, String> fetchedData, Map<String, String> calculatedMap) {

		calculatedMap.put(fetchedData.keySet().toArray()[2].toString(),
				fetchedData.get(fetchedData.keySet().toArray()[2]));
		if ((fetchedData.get(fetchedData.keySet().toArray()[0])
				.equalsIgnoreCase("GIC") && fetchedData.get(
				fetchedData.keySet().toArray()[1]).equalsIgnoreCase("Unit"))) {

			calculatedMap.put(fetchedData.keySet().toArray()[3].toString(),
					fetchedData.get(fetchedData.keySet().toArray()[3]));
			calculatedMap.put(fetchedData.keySet().toArray()[4].toString(),
					fetchedData.get(fetchedData.keySet().toArray()[4]));
		}

		return calculatedMap;
	}

	public String fetchAndSaveJiraData() {

		String status = null;
		try {
			List<Map<String, String>> jiraCalculatedMapList = jiraConnector
					.getValues();
			for (int j = 0; j < jiraCalculatedMapList.size(); j++) {

				Map<String, String> jiraCalculatedMap = new LinkedHashMap<String, String>();

				jiraCalculatedMap = jiraCalculatedMapList.get(j);

				
				 connectorService.saveAllFetchedData(jiraCalculatedMap);
				 status = "Data Saved Successfully for Jira";
				 System.out.println("final status is :" + status);
				 
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return status;
	}

}