package com.codeathon.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CodeathonAjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	private static final Logger LOG = LoggerFactory.getLogger(CodeathonAjaxAwareAuthenticationEntryPoint.class);
	private final static String DENIED_MESSAGE = "Request Denied!";
	private final static String XML_HTTP_REQUEST = "XMLHttpRequest";
	private final static String X_REQUESTED_WITH = "X-Requested-With";
	
	public CodeathonAjaxAwareAuthenticationEntryPoint(String loginUrl) {
		super(loginUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String ajaxHeader = ((HttpServletRequest) request).getHeader(X_REQUESTED_WITH);
		boolean isAjax = XML_HTTP_REQUEST.equals(ajaxHeader);
		if (isAjax) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, DENIED_MESSAGE);
		} else {
			super.commence(request, response, authException);
		}
	}
}