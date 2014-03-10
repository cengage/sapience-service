package com.sapience.connector;

import java.util.ArrayList;
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

	public List<String> fetchAndSaveJankinData() {

		List<String> statusList = new ArrayList<String>();
		String status = null;

		try {

			List<Map<String, String>> fetchedDataMapList = jenkinParseconnector
					.getValues();

			for (int i = 0; i < fetchedDataMapList.size(); i++) {

				Map<String, String> calculatedMap = new LinkedHashMap<String, String>();
				Map<String, String> fetchedData = new LinkedHashMap<String, String>();
				fetchedData = fetchedDataMapList.get(i);

				if (fetchedData.keySet().toArray()[0].toString().split(" ")[0]
						.equalsIgnoreCase("ProductId")) {

					status = "For "
							+ fetchedData.keySet().toArray()[0].toString()
							+ fetchedData
									.get(fetchedData.keySet().toArray()[0])
							+ " Data Fetching problem at "
							+ fetchedData.keySet().toArray()[1].toString()
							+ fetchedData
									.get(fetchedData.keySet().toArray()[1]);
				} else {
					calculatedMap.put(
							fetchedData.keySet().toArray()[0].toString(),
							fetchedData.get(fetchedData.keySet().toArray()[0]));
					calculatedMap.put(
							fetchedData.keySet().toArray()[1].toString(),
							fetchedData.get(fetchedData.keySet().toArray()[1]));

					if (fetchedData.get(fetchedData.keySet().toArray()[1])
							.equalsIgnoreCase("TestAutomation")) {
						calculatedMap = calculateValueForTestAutoCategoryOrGICProject(
								fetchedData, calculatedMap);

					} else if ((fetchedData.get(
							fetchedData.keySet().toArray()[0])
							.equalsIgnoreCase("GIC") && fetchedData.get(
							fetchedData.keySet().toArray()[1])
							.equalsIgnoreCase("Unit"))) {

						calculatedMap = calculateValueForTestAutoCategoryOrGICProject(
								fetchedData, calculatedMap);

					} else {

						calculatedMap = normalCalculateValueForJenkin(
								fetchedData, calculatedMap);

					}

					// ---- Save data in Database --------

					status = connectorService.saveAllFetchedData(calculatedMap);
					status = "For Product Id " + String.valueOf(i + 1) + " "+status;
				}
				System.out.println(status);
				statusList.add(status);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusList;
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

	public List<String> fetchAndSaveJiraData() {

		List<String> jiraStatusList = new ArrayList<String>();
		String status = null;
		try {
			List<Map<String, String>> jiraCalculatedMapList = jiraConnector
					.getValues();
			for (int j = 0; j < jiraCalculatedMapList.size(); j++) {

				Map<String, String> jiraCalculatedMap = new LinkedHashMap<String, String>();

				jiraCalculatedMap = jiraCalculatedMapList.get(j);

				if (jiraCalculatedMap.keySet().toArray()[2].toString().split(
						" ")[0].equalsIgnoreCase("ProductId")) {
					status = "For "
							+ jiraCalculatedMap.keySet().toArray()[2]
									.toString()
							+ jiraCalculatedMap.get(jiraCalculatedMap.keySet()
									.toArray()[2]) + " Error in Fetching Data";

				} else {

					status = connectorService
							.saveAllFetchedData(jiraCalculatedMap);
					status = "For Product Id " + String.valueOf(j + 1) +" "+ status;
				}
				System.out.println(status);
				jiraStatusList.add(status);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return jiraStatusList;
	}

}