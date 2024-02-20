package com.clouddefenseAI.service;

import com.clouddefenseAI.model.PomXmlParsedResponse;
import com.clouddefenseAI.model.UtilityModel;


public interface PomParserService {
	PomXmlParsedResponse parsePomXmlFromGithubRepository(UtilityModel accessTokenModel);
}
