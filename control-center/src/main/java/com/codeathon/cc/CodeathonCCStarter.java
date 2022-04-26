package com.codeathon.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.cabin4j.suite.cc.platform.AbstractCCPlatform;
import com.cabin4j.suite.cc.platform.configuration.RestartNodeEventListener;

@SpringBootApplication
@ComponentScan({ "com.cabin4j", "com.codeathon"})
public class CodeathonCCStarter extends AbstractCCPlatform {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CodeathonCCStarter.class);
		application.addListeners(new RestartNodeEventListener(CodeathonCCStarter.class));
		application.run(args);
	}
}
