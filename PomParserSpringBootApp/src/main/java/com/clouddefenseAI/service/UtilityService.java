package com.clouddefenseAI.service;

import com.clouddefenseAI.exception.NoDataFoundException;
import com.clouddefenseAI.exception.NoFileFoundException;
import com.clouddefenseAI.exception.PomParserException;
import com.clouddefenseAI.model.GithubRepoContent;
import com.clouddefenseAI.model.GithubRepoModel;
import com.clouddefenseAI.model.PomXmlGithubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class UtilityService {
	protected List<GithubRepoModel> getGithubRepositoriesFromGithub(final String accessToken, final String baseUrl, final RestTemplate template) {
		try {
			String apiUrl = baseUrl + "/user/repos";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			final ResponseEntity<GithubRepoModel[]> responseEntity = template.exchange(apiUrl, HttpMethod.GET, entity, GithubRepoModel[].class);
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new NoDataFoundException("No Repositories retrieved from Github!");
			}
			return Arrays.asList(responseEntity.getBody());
		} catch (NoDataFoundException exp) {
			throw exp;
		} catch (Exception exp) {
			throw new RuntimeException(exp.getMessage());
		}

	}


	protected List<GithubRepoContent> getRepoContentForSpecificRepository(final GithubRepoModel model, final String baseUrl, final String accessToken, final RestTemplate template) {
		List<GithubRepoContent> contents = new ArrayList<>();
		try {
			System.out.println("Second api hit to fetch contents for a particular repo...........");
			final String apiUrl = baseUrl + "/repos/" + model.getOwner().getLogin() + "/" + model.getName() + "/contents";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Send GET request to GitHub API
			ResponseEntity<GithubRepoContent[]> responseEntity = template.exchange(apiUrl, HttpMethod.GET, entity, GithubRepoContent[].class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				contents = List.of(responseEntity.getBody());
			}
		}  catch (Exception exp) {
			throw new NoDataFoundException(exp.getMessage());
		}
		return contents;
	}

	protected File getPomXmlForSpecificRepo(final GithubRepoModel model, final String accessToken, final String baseUrl, final RestTemplate template) {
		try {
			log.info("Third api hit to create file for the pom.xml.....");
			final String apiUrl = baseUrl + "/repos/" + model.getOwner().getLogin() + "/" + model.getName() + "/contents/pom.xml";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Fetching pom.xml as link
			ResponseEntity<PomXmlGithubResponse> responseEntity = template.exchange(apiUrl, HttpMethod.GET, entity, PomXmlGithubResponse.class);
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new NoFileFoundException("No POM.xml file retrieved!");
			}

			// Fetching exact pom.xml
			PomXmlGithubResponse pomUrl = responseEntity.getBody();
			log.info("Pom url: {}", pomUrl);

			HttpHeaders headers_2 = new HttpHeaders();
			headers_2.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity_2 = new HttpEntity<>(headers_2);
			byte[] content = null;
			ResponseEntity<byte[]> responseEntity_2 = template.exchange(pomUrl.getDownload_url(), HttpMethod.GET, entity_2, byte[].class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				content = responseEntity_2.getBody();
			}

			// logging obtained pom.xml
			String xmlContent = new String(content, StandardCharsets.UTF_8);
			System.out.println(xmlContent);

			File file = new File("pom.xml");
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(content);
			} catch (IOException exp) {
				throw new PomParserException("Failed to parse retrieved pom.xml into Java file type");
			}
			return file;
		} catch (NoFileFoundException exp) {
			throw exp;
		} catch (PomParserException exp) {
			throw exp;
		} catch (Exception exp) {
			throw new RuntimeException(exp.getMessage());
		}


	}


}
