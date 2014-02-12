package com.sapience.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sapience.service.GenericService;

public class JenkinsParseConnector {

	Logger logger = Logger.getLogger(JenkinsParseConnector.class.getName());

	GenericService genericService;

	public GenericService getGenericService() {
		return genericService;
	}

	@Inject
	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}

	public List<Map<String, String>> getValues() throws SAXException,
			IOException, ParserConfigurationException {

		List<Map<String, String>> normalizedDataMapList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> toolDataList = new ArrayList<Map<String, String>>();

		InputStream inputStream = JenkinsParseConnector.class
				.getResourceAsStream("/com/sapience/customXml/JenkinsConfigFile.xml");

		String tagName = "project";

		// fetch and parse custom xml file
		logger.info("Fetching and parsing custom xml file");

		@SuppressWarnings("unchecked")
		List<Object> objectList = (List<Object>) genericService
				.fetchAndParseXmlAsInputStream(inputStream, tagName);
		Document doc = (Document) objectList.get(0);
		NodeList nList = (NodeList) objectList.get(1);

		// do normalization on custom xml file
		toolDataList = genericService.doCustomXmlFileNormalization(nList, doc);

		normalizedDataMapList = fetchAndParseMainXmlFile(toolDataList, tagName);

		return normalizedDataMapList;
	}

	private List<Map<String, String>> fetchAndParseMainXmlFile(
			List<Map<String, String>> toolDataList, String tagName)
			throws ParserConfigurationException, FactoryConfigurationError,
			SAXException, IOException {

		List<Map<String, String>> normalizedDataMapList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < toolDataList.size(); i++) {

			Map<String, String> newToolData = new LinkedHashMap<String, String>();
			Map<String, String> normalizedDataMap = new LinkedHashMap<String, String>();
			Map<String, String> toolData = new LinkedHashMap<String, String>();

			toolData = toolDataList.get(i);
			String projectCode = toolData.get(toolData.keySet().toArray()[0]);

			String categoryName = toolData.get(toolData.keySet().toArray()[1]);

			for (int j = 3; j < toolData.size(); j++) {
				newToolData.put(toolData.keySet().toArray()[j].toString(),
						toolData.get(toolData.keySet().toArray()[j]));
			}

			String urlHref = toolData.get(toolData.keySet().toArray()[2]);
			Document mainDoc = null;
			NodeList urlNodeList = null;
			if (urlHref.contains("sonar")) {

				normalizedDataMap.put("projectCode", projectCode);
				normalizedDataMap.put("categoryName", categoryName);

			} else {
				// fetch and parse main xml
				logger.info("Fetching and parsing main xml file");
				@SuppressWarnings("unchecked")
				List<Object> mainObjectList = ((List<Object>) genericService
						.fetchAndParseXmlAsUrl(urlHref, tagName));
				mainDoc = (Document) mainObjectList.get(0);
				urlNodeList = (NodeList) mainObjectList.get(1);

				// do normalization for main xml file
				normalizedDataMap = genericService.doNormalization(projectCode,
						categoryName, urlNodeList, newToolData);
			}
			if (urlNodeList == null || urlNodeList.getLength() <= 0) {
				normalizedDataMap = fetchDataForSeleniumBDDAndUnit(toolData,
						mainDoc, normalizedDataMap, urlHref);
			}
			normalizedDataMapList.add(normalizedDataMap);

		}
		return normalizedDataMapList;
	}

	// fetch data for Selenium or BDD or sonar
	private Map<String, String> fetchDataForSeleniumBDDAndUnit(
			Map<String, String> toolData, Document mainDoc,
			Map<String, String> normalizedDataMap, String urlHref)
			throws IOException {
		if (toolData.keySet().toArray()[3].toString().equalsIgnoreCase(
				"Selenium")) {
			NamedNodeMap namedNodeMap = mainDoc.getFirstChild().getAttributes();
			if (namedNodeMap != null) {
				Node node = namedNodeMap.item(3);
				normalizedDataMap.put(
						toolData.keySet().toArray()[3].toString(),
						node.getNodeValue());
			} else {
				throw new NullPointerException();
			}
		} else if (toolData.keySet().toArray()[3].toString().equalsIgnoreCase(
				"BDD")) {
			normalizedDataMap = fetchDataForBDD(toolData, normalizedDataMap,
					mainDoc);

		} else if (toolData.keySet().toArray()[3].toString().equalsIgnoreCase(
				"Unit")) {

			org.jsoup.nodes.Document jsoupDocument = Jsoup.connect(urlHref)
					.get();
			String total = jsoupDocument.getElementsByTag("div").get(19)
					.getElementsByTag("div").get(5).text().split(" ")[0];

			normalizedDataMap.put(toolData.keySet().toArray()[3].toString(),
					total);

		} else if (toolData.get(toolData.keySet().toArray()[2]).contains(
				"sonar")) {
			normalizedDataMap = fetchDataForSonar(toolData, normalizedDataMap,
					urlHref);

		}
		return normalizedDataMap;
	}

	private Map<String, String> fetchDataForBDD(Map<String, String> toolData,
			Map<String, String> normalizedDataMap, Document mainDoc) {
		int total = 0;
		Node mainNode = mainDoc.getElementsByTagName("testsuite").item(0);

		Element elementN = (Element) mainNode;
		NodeList nodeList = elementN.getElementsByTagName("testcase");
		for (int q = 0; q < nodeList.getLength(); q++) {
			Node testCaseNode = nodeList.item(q);
			String actualString = testCaseNode.getAttributes().item(0)
					.getNodeValue().split(" ")[0];
			if (actualString.equalsIgnoreCase("Then")) {
				total = total + 1;
			}
		}
		normalizedDataMap.put(toolData.keySet().toArray()[3].toString(),
				String.valueOf(total));
		return normalizedDataMap;
	}

	private Map<String, String> fetchDataForSonar(Map<String, String> toolData,
			Map<String, String> normalizedDataMap, String urlHref)
			throws IOException {
		org.jsoup.nodes.Document jsoupDocument = Jsoup.connect(urlHref).get();
		String statements = jsoupDocument.getElementById("m_statements").text();
		String methods = jsoupDocument.getElementById("m_functions").text();
		String cyclomaticComplexity = jsoupDocument.getElementById(
				"m_function_complexity").text();
		String iniCodeCoverage = jsoupDocument.getElementById("m_coverage")
				.text();
		String codeCoverage = iniCodeCoverage.substring(0,
				iniCodeCoverage.length() - 1);

		Double statementPerMethod = Double.parseDouble(statements.replaceAll(
				",", "")) / Double.parseDouble(methods.replaceAll(",", ""));
		String spm = String.valueOf(statementPerMethod);
		normalizedDataMap.put("Unit Test Code Coverage", codeCoverage);
		normalizedDataMap.put("Statements per Method", spm);
		normalizedDataMap.put("Cyclomatic Complexity", cyclomaticComplexity);
		return normalizedDataMap;
	}

}