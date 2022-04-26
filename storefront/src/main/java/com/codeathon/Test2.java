package com.codeathon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Opportunity.Types;

public class Test2 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test2 t = new Test2();
		List<Opportunity> list = t.crawl();
		list.forEach(k -> {
			System.out.println("name " + k.getName());
			System.out.println("url " + k.getUrl());
			System.out.println("summary " + k.getSummary());
		});
	}
	public List<Opportunity> crawl() {
		try {
			System.setProperty("webdriver.chrome.driver", "src/drivers/chromedriver100v1.exe");
			List<Opportunity> list = new ArrayList<>();
			ChromeDriver driver = new ChromeDriver();
			driver.navigate().to(
					"https://www.naukri.com/information-technology-jobs?qi[]=25");
			List<String> elementUrls = new ArrayList<>();
			driver.findElements(By.cssSelector("a[class='title fw500 ellipsis']")).forEach(k -> {
				elementUrls.add(k.getAttribute("href"));
			});
			for (String element : elementUrls) {				
				driver.navigate().to(element);
				Opportunity op = new Opportunity();
				WebElement heading = driver.findElementByXPath(
						"//*[@id=\"root\"]/main/div[2]/div[2]/section[1]/div[1]/div[1]/header/h1");
				op.setName(heading.getText());
				String experience = driver.findElementByClassName("exp").getText();
				op.setExperience(experience);
				op.setType(Types.JOB);
				op.setSummary(
						driver.findElementByXPath("//*[@id=\"root\"]/main/div[2]/div[2]/section[2]").getText());
				Set<String> cat = new HashSet<>();
				driver.findElementByClassName("key-skill").findElements(By.xpath("./child::*"))
						.forEach(k -> cat.add(k.getText()));
				op.setCategories(cat);
				op.setUrl(driver.getCurrentUrl());
				op.setExtraInfo(driver.findElementByClassName("jd-stats").getText());
				list.add(op);
				
			}
			driver.close();
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
