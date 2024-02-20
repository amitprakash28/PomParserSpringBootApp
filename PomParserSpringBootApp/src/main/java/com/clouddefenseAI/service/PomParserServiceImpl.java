package com.clouddefenseAI.service;

import com.clouddefenseAI.exception.NoDataFoundException;
import com.clouddefenseAI.exception.NoFileFoundException;
import com.clouddefenseAI.exception.PomParserException;
import com.clouddefenseAI.model.*;
import com.clouddefenseAI.utility.PomXmlParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PomParserServiceImpl implements PomParserService {

	@Value("${github.baseUrl}")
	private String githubBaseUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private UtilityService utilityService;

	@Override
	public PomXmlParsedResponse parsePomXmlFromGithubRepository(final UtilityModel accessTokenModel) {
		try {
			final List<GithubRepoModel> retrievedRepos = utilityService.getGithubRepositoriesFromGithub(accessTokenModel.getAccessToken(), this.githubBaseUrl, restTemplate);
			log.info("Total retrieved repos: {}", retrievedRepos.size());
			log.info("GithubRepoModel: {}", retrievedRepos);

			Map<String, GithubRepoModel> reposWithContentMap = new LinkedHashMap<>();
			Map<String, List<GithubRepoContent>> reposWithContentListMap = new LinkedHashMap<>();

			retrievedRepos.forEach((n) -> {
				try {
					List<GithubRepoContent> contents = utilityService.getRepoContentForSpecificRepository(n, this.githubBaseUrl, accessTokenModel.getAccessToken(), restTemplate);
					if (contents.isEmpty()) {
						log.info("No content found for repo: {}", n.getFullName());
					}
					reposWithContentMap.put(n.getId(), n);
					reposWithContentListMap.put(n.getId(), contents);
				}catch (NoDataFoundException exp){
					log.info("NO data found for repo: {}", n.getId());
				}
			});

			log.info("Total repo model with contents: {}", reposWithContentListMap.values().size());


			// Get the repository which has pom.xml in it
			// Break the loop if any of the retrieved repo has pom.xml
			GithubRepoModel githubRepoModel = null;
			for (String n : reposWithContentListMap.keySet()) {
				List<GithubRepoContent> contents = reposWithContentListMap.get(n);
				for (GithubRepoContent content : contents) {
					if (content.getName().equalsIgnoreCase("pom.xml")) {
						githubRepoModel = reposWithContentMap.get(n);
						break;
					}
				}
				if (ObjectUtils.isNotEmpty(githubRepoModel)) {
					break;
				}
			}

			log.info("Github Repo to be use to read pom.xml....: {}", githubRepoModel);

			// Fetch the pom.xml from Github for this repo
			File pomXmlFile = utilityService.getPomXmlForSpecificRepo(githubRepoModel, accessTokenModel.getAccessToken(), githubBaseUrl, restTemplate);

			if(ObjectUtils.isNotEmpty(pomXmlFile)){
				System.out.println("File created and has content.....");
			}

			// Parse this file and return the response
			List<PomXmlParseContent> parseContents = PomXmlParser.parserPomXmlToJavaObject(pomXmlFile);
			return new PomXmlParsedResponse("Total " + parseContents.size() + " dependencies found!", parseContents);
		} catch (NoDataFoundException exp) {
			throw exp;
		} catch (NoFileFoundException exp) {
			throw exp;
		} catch (PomParserException exp) {
			throw exp;
		} catch (Exception exp) {
			throw exp;
		}
	}
}
