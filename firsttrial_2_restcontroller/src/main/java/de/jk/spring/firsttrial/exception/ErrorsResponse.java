package de.jk.spring.firsttrial.exception;

import java.util.ArrayList;
import java.util.List;

public class ErrorsResponse {

	private List<ErrorResponse> errorList = new ArrayList<>();

	public List<ErrorResponse> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorResponse> errorList) {
		this.errorList = errorList;
	}
	
	
	
}
