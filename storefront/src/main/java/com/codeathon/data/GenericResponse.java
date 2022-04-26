package com.codeathon.data;

import java.io.Serializable;

public class GenericResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer statusCode;
	private String status;
	private String message;
	private T data;
	
	public GenericResponse() {
		super();
	}
	
	public GenericResponse(Integer statusCode, String status, String message, T data) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}	
}