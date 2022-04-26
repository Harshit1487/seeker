package drl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cabin4j.suite.platform.dao.GenericDao;
import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Opportunity.Types;
import com.codeathon.entities.Site;

public class NaukriCrawler {
	// @code naukri-crawler

	// @global
	public GenericDao genericDao;

	// @rule naukri-crawler
	// @when $pk : Long();

	public void crawl(Long $pk) {
		// @then
		Logger log = LoggerFactory.getLogger(Object.class);

		DateFormat dateFormater = new SimpleDateFormat("dd MMM yy, hh:mm a z");

		try {
			if (null != $pk) {
				Long pk = $pk;

				Site site = genericDao.load(pk, Site.class);
				for (Opportunity o : site.getOpportunities()) {
					if (o.getLastDate().before(new Date())) {
						site.getOpportunities().remove(o);
						genericDao.remove(o);
					}
				}
				System.setProperty("webdriver.chrome.driver",
						"D:/cabin4j/projects/codeathon/storefront/src/drivers/chromedriver100v1.exe");
				ChromeDriver driver = new ChromeDriver();
				driver.navigate().to(site.getUrl().trim());
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
					genericDao.save(op);
					if (null == site.getOpportunities())
						site.setOpportunities(new ArrayList());
					site.getOpportunities().add(op);

				}

				genericDao.update(site);
				driver.close();
			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// @end

	}

}
