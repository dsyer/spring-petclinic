/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication implements Resource {

	private WebServer server;

	private Connector connector;

	public PetClinicApplication() {
		Core.getGlobalContext().register(this);
	}

	@Override
	public void afterRestore(Context<? extends Resource> context) throws Exception {
		if (connector != null) {
			connector.start();
		}
	}

	@Override
	public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
		if (connector != null) {
			connector.stop();
		}
	}

	@Bean
	public ApplicationListener<ApplicationStartedEvent> listener() {
		return event -> {
			if (event.getApplicationContext() instanceof ServletWebServerApplicationContext) {
				server = ((ServletWebServerApplicationContext) event.getApplicationContext()).getWebServer();
				var tomcat = ((TomcatWebServer) server).getTomcat();
				for (Service service : tomcat.getServer().findServices()) {
					Connector[] connectors = service.findConnectors().clone();
					if (connectors.length == 1) {
						this.connector = connectors[0];
					}
				}
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

}
