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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PetClinicApplication.class).listeners(new PropertiesLogger()).run(args);
	}

}

class PropertiesLogger implements ApplicationListener<ApplicationPreparedEvent> {

	private static final Log log = LogFactory.getLog(PropertiesLogger.class);

	private ConfigurableEnvironment environment;

	private boolean isFirstRun = true;

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		if (isFirstRun) {
			environment = event.getApplicationContext().getEnvironment();
			printProperties();
		}
		isFirstRun = false;
	}

	public void printProperties() {
		for (EnumerablePropertySource<?> source : findPropertiesPropertySources()) {
			log.info("PropertySource: " + source.getName());
			String[] names = source.getPropertyNames();
			Arrays.sort(names);
			for (String name : names) {
				String resolved = environment.getProperty(name);
				String value = source.getProperty(name).toString();
				if (resolved.equals(value)) {
					log.info(name + "=" + resolved);
				}
				else {
					log.info(name + "=" + value + " OVERRIDDEN to " + resolved);
				}
			}
		}
	}

	private List<EnumerablePropertySource<?>> findPropertiesPropertySources() {
		List<EnumerablePropertySource<?>> sources = new LinkedList<>();
		for (PropertySource<?> source : environment.getPropertySources()) {
			if (source instanceof EnumerablePropertySource enumerable) {
				sources.add(enumerable);
			}
		}
		return sources;
	}

}