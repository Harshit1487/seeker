/*
 * This file is subject to the terms and conditions defined in
 * 'Cabin4j Customer Agreement.docx', which is part of this package.
 */
package com.codeathon.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import com.cabin4j.suite.platform.constants.Constants;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.data.SearchParams;
import com.cabin4j.suite.platform.data.SearchResult;
import com.cabin4j.suite.platform.exceptions.Cabin4jException;
import com.codeathon.entities.Customer;

public class CodeathonAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CodeathonAuthenticationSuccessHandler.class);

	@Autowired
	private GenericDao genericDao;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession();
		try {
			Map<String, Object> sessionUserInfo = new HashMap<>();
			populateDetails(authentication.getPrincipal().toString().trim(), request.getParameter("user"),
					sessionUserInfo);
			sessionUserInfo.put("email", authentication.getPrincipal());
			session.setAttribute("sessionUserInfo", sessionUserInfo);
		} catch (Exception e) {
			LOG.error("Exception occured while setting user in session: " + authentication.getPrincipal().toString(),
					e);
			redirectStrategy.sendRedirect(request, response, "/logout");
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void populateDetails(String email, String userType, Map<String, Object> sessionUserInfo) {
		sessionUserInfo.put("userType", userType);
		try {

			List<SearchParams> params = new ArrayList<>();
			params.add(new SearchParams("email", Constants.QueryComparator.EQUALS, email));
			params.add(new SearchParams("active", Constants.QueryComparator.EQUALS, true));

			SearchResult<Customer> result = genericDao.loadAll(params, Customer.class, -1, -1);

			if (CollectionUtils.isEmpty(result.getResults()))
				throw new Cabin4jException("No user found in database for: " + email);

			if (result.getResults().size() > 1)
				throw new Cabin4jException("More than one user found in database for: " + email);

			Customer usr = result.getResults().get(0);
			sessionUserInfo.put("name", usr.getName());
			sessionUserInfo.put("user", usr.getUid());
			sessionUserInfo.put("pk", usr.getPk());
			if (null == usr.getCurrentLogin()) {
				usr.setLastLogin(new Date());
			} else {
				usr.setLastLogin(usr.getCurrentLogin());
			}
			usr.setCurrentLogin(new Date());
			genericDao.saveOrUpdateAndRefresh(usr);

		} catch (Exception e) {
			LOG.error("Exception occured while updating last login of user: " + email, e);
		}
	}
}
