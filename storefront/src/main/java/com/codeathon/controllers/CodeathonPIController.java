package com.codeathon.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabin4j.suite.platform.integration.v1.endpoint.PlatformIntegrationController;

@RestController
@RequestMapping("/cp/**")
public class CodeathonPIController extends PlatformIntegrationController {

}
