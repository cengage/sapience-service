package com.sapience.connector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.sapience.service.GenericService;

public class JiraParseDataConnector {

	Logger logger = Logger.getLogger(JiraParseDataConnector.class.getName());

	GenericService genericService;

	@Inject
	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}

	public List<Map<String, String>> getValues() throws SAXException,
			IOException, ParserConfigurationException, InterruptedException,
			ExecutionException, URISyntaxException {

		List<Map<String, String>> normalizedDataMapList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> toolDataList = new ArrayList<Map<String, String>>();
		InputStream inputStream = JiraParseDataConnector.class
				.getResourceAsStream("/com/sapience/customXml/JiraConfigFile.xml");

		String tagName = "project";

		@SuppressWarnings("unchecked")
		List<Object> objectList = (List<Object>) genericService
				.fetchAndParseXmlAsInputStream(inputStream, tagName);
		Document doc = (Document) objectList.get(0);
		NodeList nList = (NodeList) objectList.get(1);

		// do normalization on custom xml file
		toolDataList = genericService.doCustomXmlFileNormalization(nList, doc);

		for (int i = 0; i < toolDataList.size(); i++) {
			Map<String, String> normalizedDataMap = new LinkedHashMap<String, String>();
			Map<String, String> toolDataMap = new LinkedHashMap<String, String>();
			toolDataMap = toolDataList.get(i);
			String JIRA_URL = toolDataMap
					.get(toolDataMap.keySet().toArray()[2]);
			String JIRA_ADMIN_USERNAME = toolDataMap.keySet().toArray()[3]
					.toString();
			String JIRA_ADMIN_PASSWORD = toolDataMap.get(toolDataMap.keySet()
					.toArray()[3]);

			normalizedDataMap.put(toolDataMap.keySet().toArray()[0].toString(),
					toolDataMap.get(toolDataMap.keySet().toArray()[0]));

			normalizedDataMap.put(toolDataMap.keySet().toArray()[1].toString(),
					toolDataMap.get(toolDataMap.keySet().toArray()[1]));

			// Construct the JRJC client
			System.out.println(String.format(
					"Logging in to %s with username '%s' and password '%s'",
					JIRA_URL, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD));

			JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			URI uri = new URI(JIRA_URL);
			JiraRestClient client = factory.createWithBasicHttpAuthentication(
					uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);

			// Invoke the JRJC Client
			for (int j = 4; j < toolDataMap.size(); j++) {
				String jql = toolDataMap.get(toolDataMap.keySet().toArray()[j]);
				Promise<com.atlassian.jira.rest.client.domain.SearchResult> promiseResult = client
						.getSearchClient().searchJql(jql);

				int total = promiseResult.get().getTotal();
				normalizedDataMap.put(
						toolDataMap.keySet().toArray()[j].toString(),
						String.valueOf(total));
			}
			normalizedDataMapList.add(normalizedDataMap);
		}
		return normalizedDataMapList;

	}

}