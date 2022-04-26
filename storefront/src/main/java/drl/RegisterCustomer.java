package drl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.cabin4j.suite.cms.service.CMSi18nService;
import com.cabin4j.suite.platform.constants.Constants;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.data.SearchParams;
import com.cabin4j.suite.platform.data.SearchResult;
import com.cabin4j.suite.platform.drools.service.DroolsService;
import com.cabin4j.suite.platform.email.services.EmailService;
import com.cabin4j.suite.platform.integration.v1.data.PlatformIntegrationData;
import com.cabin4j.utility.SpringUtils;
import com.codeathon.entities.Customer;

public class RegisterCustomer {
	// @code register-customer
	// @global
	public SpringUtils springUtils;

	// @rule register-customer
	// @when $data : PlatformIntegrationData();
	void register(PlatformIntegrationData<HttpServletRequest> $data) {
		// @then
		if (null != $data.getRequest()) {

			Logger log = LoggerFactory.getLogger(Object.class);
			CMSi18nService i18n = springUtils.getBean(CMSi18nService.class);
			EmailService emailService = springUtils.getBean(EmailService.class);
			DroolsService droolsService = springUtils.getBean(DroolsService.class);
			GenericDao genericDao = springUtils.getBean(GenericDao.class);

			Map<String, String> error = new HashMap<>();
			if (null == $data.getRequest().get("email")
					|| StringUtils.isBlank((String) $data.getRequest().get("email"))) {
				error.put("email", i18n.message("register.empty.email", "Email is required."));
			} else {
				String email = (String) $data.getRequest().get("email");
				if (!emailService.isValidEmailAddress(email)) {
					error.put("email", i18n.message("register.invalid.email", "Email is invalid."));
				} else {
					try {
						List<SearchParams> params = new ArrayList<>();
			    		params.add(new SearchParams("email", Constants.QueryComparator.EQUALS, email.trim()));		    		
						SearchResult<Customer> result = genericDao.loadAll(params, Customer.class, -1, -1);
						if (CollectionUtils.isNotEmpty(result.getResults())) {
							error.put("email", i18n.message("register.email.exists", "Email already registered with us."));
						}
					} catch (Exception e) {
						log.error("Exception occurred while checking the existence of the email '{}'", email, e);
					}
				}
			}
			if (null == $data.getRequest().get("name")
					|| StringUtils.isBlank((String) $data.getRequest().get("name"))) {
				error.put("name", i18n.message("register.empty.name","Name is required."));
			}

			String password = null;
			if (null == $data.getRequest().get("password")
					|| StringUtils.isBlank((String) $data.getRequest().get("password"))) {
				error.put("password", i18n.message("register.empty.password", "Password is required."));
			} else {
				password = (String) $data.getRequest().get("password");
				if (!password.trim().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")) {
					error.put("password", i18n.message("register.invalid.password", "Password does not meet the criteria."));
				}
			}
			String cpassword = null;
			if (null == $data.getRequest().get("cpassword")
					|| StringUtils.isBlank((String) $data.getRequest().get("cpassword"))) {
				error.put("cpassword", i18n.message("register.empty.cpassword", "Confirm password is required."));
			} else {
				cpassword = (String) $data.getRequest().get("cpassword");
			}
			
			
			if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(cpassword)
					&& !password.equals(cpassword)) {
				error.put("cpassword", i18n.message("register.mismatch.cpassword", "Confirm password does not match the password."));
			}

			if (!error.isEmpty()) {
				$data.getResponse().put("errors", error);
				$data.getResponse().put("status", 400);
				$data.setResponseStatus(HttpStatus.OK);
				return;
			}
			
			try {
				Customer customer = new Customer();
				List<Object> facts = new ArrayList<>();
				facts.add(customer);
				facts.add($data.getRequest());
				droolsService.fireDRL(facts,"create-customer", false, Map.of("springUtils", springUtils));

				$data.getResponse().put("status", 200);
				$data.getResponse().put("message", i18n.message("customer.registration.successful", "Account registered successfully."));
				$data.setResponseStatus(HttpStatus.OK);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				$data.getResponse().put("error", "Exception occurred while executing register-customer command. ");
				$data.getResponse().put("message", i18n.message("customer.registration.unsuccessful", "Account registration failed."));
				$data.getResponse().put("exception", e);
				$data.getResponse().put("status", 500);
				$data.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			$data.getResponse().put("error", "Request Parameters Missing!");
			$data.getResponse().put("status", 400);
			$data.setResponseStatus(HttpStatus.BAD_REQUEST);
		}
		// @end
	}
}