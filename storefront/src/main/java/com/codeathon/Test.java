package com.codeathon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver.WindowType;

import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Opportunity.Types;

public class Test {

	private DateFormat dateFormater = new SimpleDateFormat("dd MMM yy, hh:mm a z");

	public List<Opportunity> crawl() {
		try {
			System.setProperty("webdriver.chrome.driver", "src/drivers/chromedriver100v1.exe");
			List<Opportunity> list = new ArrayList<>();
			ChromeDriver driver = new ChromeDriver();
			driver.navigate().to(
					"https://unstop.com/hackathons?filters=all,,all,all&types=oppstatus,teamsize,payment,eligible&sort=daysleft&dir=asc");
			List<String> elementUrls = new ArrayList<>();
					driver.findElementsByClassName("listing").forEach(k->{
						elementUrls.add(k.getAttribute("href"));
					});
			for (String element : elementUrls) {				
				driver.navigate().to(element);
				Opportunity op = new Opportunity();
				WebElement heading = driver.findElementByXPath(
						"//*[@id=\"s_menu\"]/div/main/app-public-competition/div/div[2]/div[1]/section/div[2]/div/div[1]/div[2]/h1");
				op.setName(heading.getText());
				String[] date = driver.findElementByXPath(
						"//*[@id=\"s_menu\"]/div/main/app-public-competition/div/div[2]/div[1]/div[2]/div/div/div[1]")
						.getText().split("-");
				op.setOpenDate(dateFormater.parse(date[0]));
				op.setLastDate(dateFormater.parse(date[1]));
				op.setType(Types.HACKATHON);
				op.setSummary(driver.findElementByXPath("//*[@id=\"tab-detail\"]").getText());
				Set<String> cat = new HashSet<>();
				driver.findElementsByClassName("ng-tns-c94-1 blue-bg-hover ng-star-inserted")
						.forEach(k -> cat.add(k.getText().replace("#", "")));
				op.setCategories(cat);
				op.setUrl(driver.getCurrentUrl());

				list.add(op);
				
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.emptyList();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t = new Test();
		List<Opportunity> list = t.crawl();
		list.forEach(k -> {
			System.out.println("name " + k.getName());
			System.out.println("url " + k.getUrl());
			System.out.println("summary " + k.getSummary());
		});
	}

}
