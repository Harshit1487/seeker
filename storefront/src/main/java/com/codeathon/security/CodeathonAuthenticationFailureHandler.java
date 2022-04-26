/*
 * This file is subject to the terms and conditions defined in
 * 'Cabin4j Customer Agreement.docx', which is part of this package.
 */
package com.codeathon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CodeathonAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(CodeathonAuthenticationFailureHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if(request.getParameter("user").equals("customer")) {
			redirectStrategy.sendRedirect(request, response,  "/login?autherror=customer");
		} else {
			redirectStrategy.sendRedirect(request, response,  "/login?autherror=chef");
		}
	}
}
