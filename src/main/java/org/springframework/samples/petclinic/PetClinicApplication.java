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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.config.HealthEndpointConfigurations;
import org.springframework.boot.config.JpaDataConfigurations;
import org.springframework.boot.config.SpringBootFeaturesApplication;
import org.springframework.boot.config.WebMvcConfigurations;
import org.springframework.context.annotation.Configuration;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootFeaturesApplication({ WebMvcConfigurations.class, JpaDataConfigurations.class,
        HealthEndpointConfigurations.class })
public class PetClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }

}

@ImportAutoConfiguration({ ThymeleafAutoConfiguration.class,
        CacheAutoConfiguration.class })
@Configuration(proxyBeanMethods = false)
class PetClinicConfigurations {
}
