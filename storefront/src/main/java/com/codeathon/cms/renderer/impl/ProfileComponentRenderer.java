package com.codeathon.cms.renderer.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cabin4j.suite.cms.renderer.CMSRenderer;
import com.cabin4j.suite.cms.service.CMSSessionService;
import com.cabin4j.suite.entity.cms.CMSComponent;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.services.GlobalPropertiesService;
import com.codeathon.entities.Customer;

@Component("profileComponentRenderer")
public class ProfileComponentRenderer implements CMSRenderer<CMSComponent> {

	private final Logger LOG = LoggerFactory.getLogger(ProfileComponentRenderer.class);

	@Autowired
	private GlobalPropertiesService globalPropertiesService;

	@Autowired
	private GenericDao genericDao;

	@Autowired
	private CMSSessionService cmsSessionService;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@SuppressWarnings({ "unchecked" })
	@Override
	public void createContext(CMSComponent component, Map<String, Object> context, Map<String, Object> pageContext) {
		try {

			Long userPk = (Long) ((Map<String, Object>) cmsSessionService.getAttribute("sessionUserInfo")).get("pk");

			Customer customer = genericDao.load(userPk, Customer.class);

			context.put("pk", customer.getPk());
			context.put("name", customer.getName());
			context.put("phone", customer.getCellphone());
			context.put("email", customer.getEmail());
			context.put("dob", null != customer.getDob() ? dateFormat.format(customer.getDob()) : null);
			if (null != customer.getImage()) {
				if (StringUtils.isNotBlank(customer.getImage().getUrl()))
					context.put("img", customer.getImage().getUrl());
				if (StringUtils.isNotBlank(customer.getImage().getAltText()))
					context.put("altText", customer.getImage().getAltText());

			}
		} catch (

		Exception e) {
			LOG.error("Exception occured while getting profile data for user . Redirecting to home page", e);
		}
	}
}