package com.clouddefenseAI.utility;

import com.clouddefenseAI.exception.PomParserException;
import com.clouddefenseAI.model.PomXmlParseContent;
import com.clouddefenseAI.model.PomXmlParsedResponse;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class PomXmlParser {
	public static List<PomXmlParseContent> parserPomXmlToJavaObject(File pomXmlFile) {
		List<PomXmlParseContent> responseList = new ArrayList<>();
		try {
			System.out.println("4th api call, to parse pom.xml.....");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			System.out.println("About to parse file....");
//			Document doc = dBuilder.parse(pomXmlFile);
			Document doc = dBuilder.parse(new FileInputStream(pomXmlFile));
			System.out.println("File parsed into Document....");

			doc.getDocumentElement().normalize();
			NodeList dependencyList = doc.getElementsByTagName("dependency");
			for (int i = 0; i < dependencyList.getLength(); i++) {
				Node dependencyNode = dependencyList.item(i);
				if (dependencyNode.getNodeType() == Node.ELEMENT_NODE) {
					Element dependencyElement = (Element) dependencyNode;
					String artifactId = dependencyElement.getElementsByTagName("artifactId").item(0).getTextContent();
					// Check if version element exists
					NodeList versionList = dependencyElement.getElementsByTagName("version");
					String version = "No version found";
					if (versionList.getLength() > 0) {
						version = versionList.item(0).getTextContent();
					}
					log.info("ArtifactId: {}", artifactId);
					log.info("Version: {}", version);
					System.out.println(" ");

					responseList.add(new PomXmlParseContent(artifactId, version));
				}

				pomXmlFile.delete();
			}
		} catch (Exception exp) {
			log.error("Error while parsing pom.xml using parser: {}", exp.getMessage());
			throw new PomParserException(exp.getMessage());
		}
		return responseList;
	}
}
