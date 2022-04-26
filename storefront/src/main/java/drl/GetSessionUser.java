package drl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cabin4j.suite.platform.integration.v1.data.PlatformIntegrationData;
import com.cabin4j.utility.SpringUtils;

public class GetSessionUser {
	// @code get-session-user

	// @global
	public SpringUtils springUtils;

	// @rule get-session-user
	// @when $data : PlatformIntegrationData();
	void getCart(PlatformIntegrationData<HttpServletRequest> $data) {
		// @then
		Logger log = LoggerFactory.getLogger(Object.class);
		try {
			if (!(SecurityContextHolder.getContext().getAuthentication()instanceof AnonymousAuthenticationToken)) {
				$data.getResponse().put("loggedIn", "true");
				if (null != $data.getServletRequest() ) {
					HttpServletRequest req = (HttpServletRequest) $data.getServletRequest();
					$data.getResponse().putAll((Map) req.getSession().getAttribute("sessionUserInfo"));
				}
			} else {
				$data.getResponse().putAll(Map.of("loggedIn", "false", "user", "anonymous"));
			}
			$data.getResponse().put("status", 200);
			$data.setResponseStatus(HttpStatus.OK);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Exception occurred while getting session user.", e);
			} else {
				log.warn("Exception occurred while getting session user.");
			}
			$data.getResponse().put("error", "Exception occurred while getting session user.");
			$data.getResponse().put("status", 500);
			$data.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			return;
		}
		// @end
	}
}
