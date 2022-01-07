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

package org.springframework.samples.petclinic.vet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;

/**
 * Test class for {@link MapVetRepository}
 *
 * @author Dave Syer
 */
class MapVetRepositoryTests {

	private KeyValueTemplate template = new KeyValueTemplate(new MapKeyValueAdapter(new ConcurrentHashMap<>()));

	private MapVetRepository vets = new MapVetRepository(template);

	private Vet helen = helen();

	private Vet helen() {
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		return helen;
	};

	@BeforeEach
	void setup() {
		template.insert(helen.getId(), helen);
	}

	@Test
	void testFindAllPageable() throws Exception {
		Page<Vet> result = vets.findAll(Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void testFindAll() throws Exception {
		Collection<Vet> result = vets.findAll();
		assertThat(result).hasSize(1);
	}

}
