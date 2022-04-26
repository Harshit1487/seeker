package com.codeathon.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabin4j.suite.entity.platform.EmailMessage;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.drools.service.DroolsService;
import com.cabin4j.suite.platform.email.services.EmailService;
import com.codeathon.entities.Customer;
import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Site;

@RestController
public class CarwlController {
	@Autowired
	GenericDao genericDao;

	@Autowired
	DroolsService droolsService;

	@Autowired
	EmailService emailService;

	@Autowired
	EntityManager entitiyManager;

	public boolean matches(Set<String> customerCat, Set<String> oppCat, String summary) {
		for (String s : customerCat) {
			if (oppCat.contains(s) || summary.contains(s))
				return true;
		}

		return false;

	}

	public boolean check(String s) {
		if (null == s || StringUtils.isBlank(s))
			return false;
		return true;

	}

	@GetMapping("/crawl")
	public void crawl(@RequestParam long pk) {
		try {
			Site site = genericDao.load(pk, Site.class);
			if (null != site.getDrool())
				droolsService.fireDRL(List.of(site.getPk()), site.getDrool(),
						Collections.singletonMap("genericDao", genericDao));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("/crawl/email")
	public void crawlEmail() {
		List<Customer> customers = entitiyManager
				.createQuery("SELECT var FROM Customer var WHERE var.active = true", Customer.class).getResultList();
		try {
			List<Opportunity> opprtunities = genericDao.loadAll(Collections.emptyList(), Opportunity.class, -1, -1)
					.getResults();

			if (CollectionUtils.isNotEmpty(customers) && CollectionUtils.isNotEmpty(opprtunities)) {
				for (Opportunity opportunity : opprtunities) {
					for (Customer customer : customers)
						if (emailService.isValidEmailAddress(customer.getEmail().trim())) {
							if (matches(customer.getCategories(), opportunity.getCategories(),
									opportunity.getSummary())) {
								EmailMessage message = new EmailMessage();
								message.setTo(Collections.singletonList(customer.getEmail().trim()));
								message.setTemplateId("send-opportunity");//
								message.setFrom("jharshit10101@gmail.com");//
								Map<String, Object> map = new HashMap<>();
								map.put("name", customer.getName());
								if (check(opportunity.getName()))
									map.put("heading", opportunity.getName());
								else
									map.put("heading","");
								if (null != opportunity.getLastDate() && check(opportunity.getLastDate().toString()))
									map.put("lastDate", opportunity.getLastDate().toString());
								else
									map.put("lastDate","");					
								if (null != opportunity.getOpenDate() && check(opportunity.getOpenDate().toString()))
									map.put("openDate", opportunity.getOpenDate().toString());
								else
									map.put("openDate","");
								if (check(opportunity.getUrl()))
									map.put("url", opportunity.getUrl());
								else
									map.put("url","");
								if (check(opportunity.getSummary()))
									map.put("summary", opportunity.getSummary());
								else
									map.put("summary","");
								if (check(opportunity.getExperience()))
									map.put("experience", opportunity.getExperience());
								else
									map.put("experience","");
								if (check(opportunity.getExtraInfo()))
									map.put("extraInfo", opportunity.getExtraInfo());
								else
									map.put("extraInfo","");
								genericDao.saveOrUpdateAndRefresh(message);

								emailService.sendEmailAsync(message, map);
							}
						}
				}
			}
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
