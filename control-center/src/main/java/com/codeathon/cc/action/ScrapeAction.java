package com.codeathon.cc.action;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cabin4j._form.action.Cabin4jEntityAction;
import com.cabin4j.entity.Cabin4jEntitySchema;
import com.cabin4j.exception.Cabin4jException;
import com.cabin4j.suite.cc.platform.service.DroolsService;
import com.codeathon.entities.Site;

@Component("siteCrawlAction")
public class ScrapeAction  implements Cabin4jEntityAction<Site> {


	
	@Autowired
	DroolsService droolsService;
	
	@Override
	public String executeAction(Site entity, Cabin4jEntitySchema schema) throws Cabin4jException {
		try{if(null != entity.getDrool())
			droolsService.fireDRL(List.of(entity.getPk()), entity.getDrool(), Collections.emptyMap());
		return "success";}
		catch (Exception e) {
		return e.getLocalizedMessage();
		}
	}

}
