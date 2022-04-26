package com.codeathon.service;
public interface PassswordService {
	
	boolean validatePassword(String password, String encoded);
	
	String encodePassword(String password);
}
