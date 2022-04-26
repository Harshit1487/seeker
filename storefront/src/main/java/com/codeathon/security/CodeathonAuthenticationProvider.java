/*
 * This file is subject to the terms and conditions defined in
 * 'Cabin4j Customer Agreement.docx', which is part of this package.
 */
package com.codeathon.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import com.cabin4j.suite.entity.platform.PrincipalGroup;
import com.cabin4j.suite.platform.constants.Constants;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.data.SearchParams;
import com.cabin4j.suite.platform.data.SearchResult;
import com.cabin4j.suite.platform.exceptions.Cabin4jException;
import com.codeathon.entities.Customer;
import com.codeathon.service.PassswordService;

public class CodeathonAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(CodeathonAuthenticationProvider.class);

	@Autowired
	private GenericDao genericDao;

	@Autowired
	private PassswordService passswordService;

	@Autowired
	private HttpServletRequest request;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String email = authentication.getName().trim();
		String password = authentication.getCredentials().toString().trim();

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
			String encodedPW = passswordService.encodePassword(password);
			if (null != usr) {
				if (usr.getEncodedPassword().equals(encodedPW)) {
					List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
					grantedAuths.add(new SimpleGrantedAuthority("customer"));
						return new UsernamePasswordAuthenticationToken(email, encodedPW,grantedAuths);					
				}
			} else {
				LOG.error("Unable to get user: " + email);
			}

		} catch (Exception e) {
			LOG.error("Exception occured while authenticating user: " + email, e);
		}
		return null;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
