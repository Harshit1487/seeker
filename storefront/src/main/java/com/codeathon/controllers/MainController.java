package com.codeathon.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cabin4j.suite.cms.service.CMSService;
import com.cabin4j.suite.cms.service.CMSSessionService;
import com.cabin4j.suite.cms.service.CMSSiteService;

@Controller
public class MainController extends AbstractPageController{

	@Autowired
	private CMSService cmsService;
	
	@Autowired
	private CMSSiteService cmsSiteService;	
	
	@Autowired
	private CMSSessionService cmsSessionService;
	
	@GetMapping("/**")
	public ModelAndView getPage(HttpServletRequest request) {
		String pageId = cmsService.resolvePageId(request.getRequestURI(), cmsSiteService.getActiveCatalogueForSite(getSiteId()), false);
		if (StringUtils.isBlank(pageId) || pageId.trim().equals("/")) {
			pageId = cmsService.resolvePageId(cmsSiteService.getStartingPage(getSiteId()), cmsSiteService.getActiveCatalogueForSite(getSiteId()), false);
		}
		return getEditablePageModelAndView(StringUtils.removeStart(pageId, "/"), null);
	}
}