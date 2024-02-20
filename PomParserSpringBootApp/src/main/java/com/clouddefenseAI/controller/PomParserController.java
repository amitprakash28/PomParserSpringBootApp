package com.clouddefenseAI.controller;

import com.clouddefenseAI.exception.NoDataFoundException;
import com.clouddefenseAI.exception.NoFileFoundException;
import com.clouddefenseAI.exception.PomParserException;
import com.clouddefenseAI.model.UtilityModel;
import com.clouddefenseAI.service.PomParserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pomParser/")
public class PomParserController {
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Autowired
	private PomParserService pomParserService;


	@Operation(summary = "Get access token from Github")
	@GetMapping("/accessToken/get")
	public UtilityModel getAccessToken(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
				authentication.getAuthorizedClientRegistrationId(), authentication.getName());

		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		String tokenValue = accessToken.getTokenValue();

		System.out.println("Token: " + tokenValue);

		UtilityModel model = new UtilityModel();
		model.setAccessToken(tokenValue);
		return model;
	}



	@Operation(summary = "Use github access token to parse pom.xml of a repo")
	@GetMapping("/parse")
	public ResponseEntity parsePomXmlFromGithubRepositoryHandler(@RequestParam String at) {
		try {
			return new ResponseEntity(pomParserService.parsePomXmlFromGithubRepository(new UtilityModel(at)), HttpStatus.OK);
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
