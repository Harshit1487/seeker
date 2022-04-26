package drl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cabin4j.suite.platform.dao.GenericDao;
import com.codeathon.entities.Opportunity;
import com.codeathon.entities.Opportunity.Types;
import com.codeathon.entities.Site;

public class D2CCrawler {
	// @code d2c-crawler

	// @global
	public GenericDao genericDao;

	// @rule d2c-crawler
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
				driver.findElementsByClassName("listing").forEach(k -> {
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
