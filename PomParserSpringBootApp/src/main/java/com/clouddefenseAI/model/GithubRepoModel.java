package com.clouddefenseAI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GithubRepoModel {
	private String id;
	private String name;
	private String fullName;
	private String description;
	private GithubRepoOwner owner;


}
