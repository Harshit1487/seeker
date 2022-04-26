package com.codeathon.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import com.cabin4j.suite.cms.data.CMSPageData;
import com.cabin4j.suite.cms.data.LocaleData;
import com.cabin4j.suite.cms.service.CMSService;
import com.cabin4j.suite.cms.service.CMSSiteService;
import com.cabin4j.suite.entity.platform.enums.Version;

public abstract class AbstractPageController {
	
	@Autowired
	private CMSService cmsService;

	@Autowired
	private CMSSiteService cmsSiteService;
	
	@Value("${cabin4j.cms.active.version:Online}")
	private String version;
	
	@Value("${cabin4j.cms.active.site:codeathon}")
	private String siteId;

	protected ModelAndView getEditablePageModelAndView(String pageId, Map<String, Object> additionalParameters) {
		LocaleData locale = cmsSiteService.defaultSiteLocale(siteId);
		CMSPageData data = cmsService.getPage(pageId, true, true, additionalParameters,
				cmsSiteService.getActiveCatalogueForSite(siteId), Version.valueOf(version), locale.getCode(), true);
		ModelAndView model = new ModelAndView();
		if(null !=  data.getContext()) {
			model.setViewName((String) data.getContext().get("viewName"));
		}		
		model.addObject("pageContent", data.getHtml());
		model.addObject("title", data.getTitle());
		model.addAllObjects(data.getContext());
		return model;
	}

	public String getVersion() {
		return version;
	}

	public String getSiteId() {
		return siteId;
	}
}
