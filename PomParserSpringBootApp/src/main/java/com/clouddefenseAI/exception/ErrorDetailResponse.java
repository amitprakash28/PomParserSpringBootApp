package com.clouddefenseAI.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDetailResponse {
	private String message;
	private LocalDateTime timestamp;
	private String handler;
	private Integer statusCode;
}
