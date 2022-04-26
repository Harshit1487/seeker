package com.codeathon.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.cabin4j.suite.platform.dao.GenericDao;
import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Opportunity.Types;
import com.codeathon.entities.Site;
import com.codeathon.service.CrawlSite;

public class D2CCrawler implements CrawlSite {
	
	@Autowired
	GenericDao genericdDao;
	
	private DateFormat dateFormater = new SimpleDateFormat("dd MMM yyyy, hh:mm:ss a z");

	@Override
	public void crawl(Long pk) {
		try {

			Site site = genericdDao.load(pk, Site.class);
			for(Opportunity o : site.getOpportunities()){
				if(o.getLastDate().before(new Date())) {
					site.getOpportunities().remove(o);
					genericdDao.remove(o);
				}
			}
			ChromeDriver driver = new ChromeDriver();
			driver.navigate().to(site.getUrl());
			List<WebElement> elements = driver.findElementsByClassName("listing");
              for(WebElement element : elements) {
            	driver.navigate().to(element.getAttribute("href"));          	
            	Opportunity op =  new Opportunity();
            	WebElement heading = driver.findElementByXPath("//*[@id=\"s_menu\"]/div/main/app-public-competition/div/div[2]/div[1]/section/div[2]/div/div[1]/div[2]/h1");
            	op.setName(heading.getText());
            	String[] date = driver.findElementByXPath("//*[@id=\"s_menu\"]/div/main/app-public-competition/div/div[2]/div[1]/div[2]/div/div/div[1]").getText().split("-");
            	op.setOpenDate(dateFormater.parse(date[0]));
            	op.setLastDate(dateFormater.parse(date[1]));
            	op.setType(Types.HACKATHON );
            	op.setSummary(driver.findElementByXPath("//*[@id=\"tab-detail\"]").getText());
            	Set<String> cat = new HashSet<>();
            	driver.findElementsByClassName("ng-tns-c94-1 blue-bg-hover ng-star-inserted").forEach(k->cat.add(k.getText().replace("#", "")));
            	op.setCategories(cat);
            	op.setUrl(driver.getCurrentUrl());
            	genericdDao.save(op);
            	if(null == site.getOpportunities())
            		site.setOpportunities(new ArrayList());
            	site.getOpportunities().add(op);
              }
              genericdDao.update(site);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
