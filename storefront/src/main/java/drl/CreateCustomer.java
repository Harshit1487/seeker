package drl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cabin4j.suite.entity.platform.EmailMessage;
import com.cabin4j.suite.entity.platform.PrincipalGroup;
import com.cabin4j.suite.platform.constants.Constants;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.data.SearchParams;
import com.cabin4j.suite.platform.data.SearchResult;
import com.cabin4j.suite.platform.email.services.EmailService;
import com.cabin4j.suite.platform.exceptions.Cabin4jException;
import com.cabin4j.suite.platform.services.GlobalPropertiesService;
import com.cabin4j.utility.SpringUtils;
import com.codeathon.entities.Customer;
import com.codeathon.service.PassswordService;

public class CreateCustomer {
	// @code create-customer
	
	// @global
	public SpringUtils springUtils;

    // @rule create-customer
    // @when $profileData : Map(); $customer: Customer();
    void getProfile(Map<String, Object> $profileData, Customer $customer) {
    	// @then
    	GenericDao genericDao = springUtils.getBean(GenericDao.class);
    	PassswordService passswordService = springUtils.getBean(PassswordService.class);
    	EmailService emailService = springUtils.getBean(EmailService.class);
    	GlobalPropertiesService globalPropertiesService = springUtils.getBean(GlobalPropertiesService.class);
    	Logger log = LoggerFactory.getLogger(Object.class);
    	try {
			$customer.setUid(((String)$profileData.get("email")).trim());
			$customer.setEmail(((String)$profileData.get("email")).trim());
			$customer.setName(((String)$profileData.get("name")).trim());
			$customer.setActive(true);
			if (null != $profileData.get("cellphone") && StringUtils.isNotBlank((String)$profileData.get("cellphone"))) {
				$customer.setCellphone(((String)$profileData.get("cellphone")).trim());
			}
			if (null != $profileData.get("password") && StringUtils.isNotBlank((String)$profileData.get("password"))) {
				$customer.setEncodedPassword(passswordService.encodePassword(((String)$profileData.get("password")).trim()));
			}
			if (null != $profileData.get("interests") && StringUtils.isNotBlank((String)$profileData.get("interests"))) {				
				Set<String> cat = new HashSet<>();
				String[] categories = ((String)$profileData.get("interests")).split(",");
				for(String s : categories) {
					cat.add(s);
				}
				$customer.setCategories(cat);
				
			}
			
			
			genericDao.saveOrUpdateAndRefresh($customer);

			EmailMessage message = new EmailMessage();
			message.setTo(Collections.singletonList($customer.getEmail()));
			message.setTemplateId("user-registration");
			
			message.setFrom(globalPropertiesService.getPropertyFromCache("chefpod.email.from", null));
			genericDao.saveOrUpdateAndRefresh(message);
			emailService.sendEmailAsync(message, Collections.singletonMap("customer", $customer));
		} catch (Cabin4jException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error("Exception occured while registering customer with email '{}'.", ((String)$profileData.get("email")).trim(), e);
			throw new Cabin4jException("Exception occured while registering customer with email: " + ((String)$profileData.get("email")).trim());
		}
        // @end
    }
}