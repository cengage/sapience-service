package com.sapience.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sapience.service.GenericService;

public class GenericServiceImpl implements GenericService {
	Logger logger = Logger.getLogger(GenericServiceImpl.class.getName());

	@Override
	public Object fetchXml() {
		return null;
	}

	// Fetch And parse xml on given inputStream with main tag name
	public List<Object> fetchAndParseXmlAsInputStream(InputStream inputStream,
			String tagName) throws SAXException, IOException,
			ParserConfigurationException, FactoryConfigurationError {

		List<Object> objectList = new ArrayList<Object>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);

		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName(tagName);

		objectList.add(doc);
		objectList.add(nodeList);

		return objectList;
	}

	// Fetch And parse xml on given url with main tag name
	public List<Object> fetchAndParseXmlAsUrl(String url, String tagName)
			throws ParserConfigurationException, FactoryConfigurationError,
			SAXException, IOException {

		logger.info("parsing custom xml");
		List<Object> objectList = new ArrayList<Object>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(url);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName(tagName);
		objectList.add(doc);
		objectList.add(nodeList);
		return objectList;
	}

	// Do Normalization for main xml file
	public Map<String, String> doNormalization(String projectCode,
			String categoryName, NodeList urlNodeList,
			Map<String, String> newToolData) {

		Map<String, String> normalizedMapData = new LinkedHashMap<String, String>();
		normalizedMapData.put("projectCode", projectCode);
		normalizedMapData.put("categoryName", categoryName);

		for (int temp1 = 0; temp1 < urlNodeList.getLength(); temp1++) {

			Node nNode = urlNodeList.item(temp1);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				for (Map.Entry<String, String> entry : newToolData.entrySet()) {

					String newOutput = getNodeValueByTagName(eElement,
							entry.getValue(), entry.getKey(), 0);

					normalizedMapData.put(entry.getKey(), newOutput);
				}

			}
		}
		return normalizedMapData;
	}

	// Do Normalization for custom xml file
	public List<Map<String, String>> doCustomXmlFileNormalization(
			NodeList nList, Document doc) {

		List<Map<String, String>> mainMapList = new ArrayList<Map<String, String>>();

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Map<String, String> normalizedMapData = new LinkedHashMap<String, String>();
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String projectId = eElement.getAttributes().item(0)
						.getNodeValue();
				String projectName = eElement.getAttributes().item(1)
						.getNodeValue();

				normalizedMapData.put(projectId, projectName);

				String categoryName = getNodeValueByTagName(eElement,
						"categories", "name", 0);

				normalizedMapData.put("categoryName", categoryName);

				String urlName = getNodeValueByTagName(eElement, "url", "name",
						0);

				String urlHref = getNodeValueByTagName(eElement, "url", "href",
						0);

				normalizedMapData.put(urlName, urlHref);
				String userName = "";
				String password = "";
				try {
					userName = getNodeValueByTagName(eElement, "user", "name",
							0);

					password = getNodeValueByTagName(eElement, "password",
							"pwd", 0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!userName.equalsIgnoreCase("")
						&& !password.equalsIgnoreCase("")) {
					normalizedMapData.put(userName, password);
				}
				// fetch Sub_categories Data
				normalizedMapData = fetchSubCategoriesData(eElement,
						normalizedMapData);
			}
			mainMapList.add(normalizedMapData);
		}
		return mainMapList;
	}

	private Map<String, String> fetchSubCategoriesData(Element eElement,
			Map<String, String> normalizedMapData) {

		NodeList categoryList = eElement.getElementsByTagName("category");

		for (int a = 0; a < categoryList.getLength(); a++) {
			Node catNode = categoryList.item(a);
			if (catNode.getNodeType() == Node.ELEMENT_NODE) {
				Element catElement = (Element) catNode;
				String subCategoryName = catElement.getAttribute("name");

				String tagName = getNodeValueByTagName(catElement,
						"identifier", "value", 0);

				normalizedMapData.put(subCategoryName, tagName);

			}
		}
		return normalizedMapData;
	}

	public String getNodeValueByTagName(Element eElement, String tagName,
			String categoryName, int itemNumber) {

		return eElement.getElementsByTagName(tagName).item(itemNumber)
				.getAttributes().getNamedItem(categoryName).getNodeValue();
	}
}