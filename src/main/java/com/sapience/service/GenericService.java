package com.sapience.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public interface GenericService {
	
	// method to fetch the xml file
	Object fetchXml();

	// method to fetch and parse xml file
	Object fetchAndParseXmlAsInputStream(InputStream inputStream, String tagName)
			throws SAXException, IOException, ParserConfigurationException;

	Object fetchAndParseXmlAsUrl(String url, String tagName)
			throws ParserConfigurationException, FactoryConfigurationError,
			SAXException, IOException;

	/* Object fetchAndParseXmlAsUrl(String url, String tagName); */

	// method to do normalization
	Map<String, String> doNormalization(String projectCode,
			String categoryName, NodeList urlNodeList,
			Map<String, String> newToolData);

	// do normalization on custom file
	List<Map<String, String>> doCustomXmlFileNormalization(NodeList nList,
			Document doc);
}