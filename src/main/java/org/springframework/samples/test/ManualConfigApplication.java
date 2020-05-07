/*
 * Copyright 2012-2018 the original author or authors.
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

package org.springframework.samples.test;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.servlet.ServletManagementContextAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.devtools.autoconfigure.DevToolsDataSourceAutoConfiguration;
import org.springframework.boot.devtools.autoconfigure.LocalDevToolsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootConfiguration(proxyBeanMethods = false)
@ImportAutoConfiguration({ MessageSourceAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
		DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, TransactionAutoConfiguration.class,
		WebMvcAutoConfiguration.class, ServletWebServerFactoryAutoConfiguration.class, ErrorMvcAutoConfiguration.class,
		DispatcherServletAutoConfiguration.class, ThymeleafAutoConfiguration.class, CacheAutoConfiguration.class,
		ConfigurationPropertiesAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class })
@Import({ ApplicationActuatorConfiguration.class, DevToolsConfiguration.class })
@EntityScan(basePackageClasses = { Owner.class, Vet.class, Visit.class })
@EnableJpaRepositories(basePackageClasses = { Owner.class, Vet.class, Visit.class }, bootstrapMode = BootstrapMode.LAZY)
public class ManualConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManualConfigApplication.class, args);
	}

	// Need this because we have `@EnableJpaRepositories` (because not in parent package
	// of repository interfaces)
	@Bean
	public EntityManagerFactoryBuilderCustomizer entityManagerFactoryBootstrapExecutorCustomizer(
			Map<String, AsyncTaskExecutor> taskExecutors) {
		return (builder) -> {
			builder.setBootstrapExecutor(new ConcurrentTaskExecutor());
		};
	}

}

@ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration({ EndpointAutoConfiguration.class, HealthContributorAutoConfiguration.class,
		HealthEndpointAutoConfiguration.class, InfoEndpointAutoConfiguration.class, WebEndpointAutoConfiguration.class,
		ServletManagementContextAutoConfiguration.class, ManagementContextAutoConfiguration.class })
class ApplicationActuatorConfiguration {

}

@ConditionalOnClass(name = "org.springframework.boot.devtools.autoconfigure.DevToolsDataSourceAutoConfiguration")
@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration({ DevToolsDataSourceAutoConfiguration.class, LocalDevToolsAutoConfiguration.class })
class DevToolsConfiguration {

}