package com.codeathon.cms.renderer.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cabin4j.suite.cms.renderer.CMSRenderer;
import com.cabin4j.suite.entity.cms.CMSComponent;

@Component("loginComponentRenderer")
public class LoginComponentRenderer implements CMSRenderer<CMSComponent> {
	
	private final Logger LOG = LoggerFactory.getLogger(LoginComponentRenderer.class);

	@Override
	public void createContext(CMSComponent component, Map<String, Object> context, Map<String, Object> pageContext) {
		if(null != pageContext && pageContext.containsKey("parameters")) {
			context.put("parameters", pageContext.get("parameters"));
		}
	}


}
