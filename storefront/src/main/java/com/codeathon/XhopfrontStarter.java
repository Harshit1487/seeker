package com.codeathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cabin4j.suite.platform.RestartNodeEventListener;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.cabin4j","com.codeathon" })
@EntityScan(basePackages = { "com.cabin4j.suite.entity","com.codeathon.entities"})
public class XhopfrontStarter implements WebMvcConfigurer{

	public static void main(String[] args) {
		//System.setProperty("es.set.netty.runtime.available.processors", "false");// required for
																					// micrometer-elasticsearch
		SpringApplication application = new SpringApplication(XhopfrontStarter.class);
		application.addListeners(new RestartNodeEventListener(XhopfrontStarter.class));
		application.run(args);
	}

}
