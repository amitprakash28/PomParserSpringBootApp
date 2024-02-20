package com.clouddefenseAI.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
@Slf4j
public class JavaParserExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handlerException2(MethodArgumentNotValidException exp, WebRequest request) {
		ErrorDetailResponse errorResponse = new ErrorDetailResponse();
		exp.getBindingResult().getAllErrors().forEach((n) -> errorResponse.setMessage(n.getDefaultMessage()));
		errorResponse.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		errorResponse.setHandler(request.getDescription(false));
		return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(PomParserException.class)
	public ResponseEntity pomParserExceptionHandler(PomParserException exp, WebRequest request) {
		ErrorDetailResponse errorResponse = new ErrorDetailResponse();
		exp.getBindingResult().getAllErrors().forEach((n) -> errorResponse.setMessage(n.getDefaultMessage()));
		errorResponse.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		errorResponse.setHandler(request.getDescription(false));
		return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity noDataFoundExceptionHandler(NoDataFoundException exp, WebRequest request) {
		ErrorDetailResponse errorResponse = new ErrorDetailResponse();
		exp.getBindingResult().getAllErrors().forEach((n) -> errorResponse.setMessage(n.getDefaultMessage()));
		errorResponse.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		errorResponse.setHandler(request.getDescription(false));
		return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoFileFoundException.class)
	public ResponseEntity noFileFoundExceptionHandler(NoFileFoundException exp, WebRequest request) {
		ErrorDetailResponse errorResponse = new ErrorDetailResponse();
		exp.getBindingResult().getAllErrors().forEach((n) -> errorResponse.setMessage(n.getDefaultMessage()));
		errorResponse.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		errorResponse.setHandler(request.getDescription(false));
		return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(Exception.class)
	public ResponseEntity globalExceptionHandler(Exception exp, WebRequest request) {
		ErrorDetailResponse errorResponse = new ErrorDetailResponse();
		errorResponse.setMessage("Invalid Request");
		errorResponse.setTimestamp(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		errorResponse.setHandler(request.getDescription(false));
		errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		log.error("Exception class error: {}", exp.getMessage());
		return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
