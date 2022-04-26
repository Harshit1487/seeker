package com.codeathon.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.codeathon.constants.CPConstants;

@Controller
public class AuthenticationPageController extends AbstractPageController {

	private static final String REDIRECT_LOGIN_LOGOUT = "redirect:/login";

  
	@GetMapping(CPConstants.URL.LOGIN)
	public ModelAndView getlogin(@RequestParam(value = "autherror", required = false) String autherror,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			return new ModelAndView("redirect:/");
		}
		Map<String, Object> params = new HashMap<>();
		if (null != autherror) {
			params.put("autherror", "login.authentication.error");
		}
		Iterable<ClientRegistration> clientRegistrations = null;
	    
	    

		

		return getEditablePageModelAndView(CPConstants.Page.LOGIN, params);
	}
	
	@GetMapping(CPConstants.URL.REGISTER)
	public ModelAndView getRegister(HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			return new ModelAndView("redirect:/");
		}
		Iterable<ClientRegistration> clientRegistrations = null;
	   
	   
	    Map<String, Object> params = new HashMap<>();
		

		return getEditablePageModelAndView(CPConstants.Page.REGISTER, params);
	}

	@GetMapping(CPConstants.URL.LOGOUT)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return REDIRECT_LOGIN_LOGOUT;
	}	
}