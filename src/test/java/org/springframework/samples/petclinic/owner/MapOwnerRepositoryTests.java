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

package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;
import org.springframework.samples.petclinic.system.PredicateQueryEngine;

/**
 * Test class for {@link MapOwnerRepository}
 *
 * @author Dave Syer
 */
class MapOwnerRepositoryTests {

	private KeyValueTemplate template = new KeyValueTemplate(
			new MapKeyValueAdapter(new ConcurrentHashMap<>(), new PredicateQueryEngine<>()));

	private OwnerRepository owners = new MapOwnerRepository(template);

	private Owner george = george();

	private Owner george() {
		Owner george = new Owner();
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");
		Pet max = new Pet();
		PetType dog = new PetType();
		dog.setName("dog");
		max.setType(dog);
		max.setName("Max");
		max.setBirthDate(LocalDate.now());
		george.addPet(max);
		return george;
	};

	@BeforeEach
	void setup() {

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		george.getPet("Max").getVisits().add(visit);
		owners.save(george);
		PetType dog = new PetType();
		dog.setName("dog");
		dog.setId(1);
		template.insert(1, dog);

	}

	@Test
	void testFindPetTypes() throws Exception {
		List<PetType> result = owners.findPetTypes();
		assertThat(result).hasSize(1);
	}

	@Test
	void testFindById() throws Exception {
		Owner result = owners.findById(george.getId());
		assertThat(result).isNotNull();
	}

	@Test
	void testFindAll() throws Exception {
		Page<Owner> result = owners.findAll(Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void testFindByLastNameEmpty() throws Exception {
		Page<Owner> result = owners.findByLastName("", Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void testFindByLastNameExact() throws Exception {
		Page<Owner> result = owners.findByLastName("Franklin", Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void testFindByLastNamePartial() throws Exception {
		Page<Owner> result = owners.findByLastName("rank", Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	void testFindByLastNamePage() throws Exception {
		owners.save(new Owner());
		owners.save(george());
		owners.save(george());
		owners.save(new Owner());
		owners.save(new Owner());
		owners.save(george());
		Page<Owner> result = owners.findByLastName("Franklin", Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(4);
		assertThat(result.getTotalElements()).isEqualTo(4);
	}

	@Test
	void testFindByLastNameMultiPage() throws Exception {
		owners.save(george());
		owners.save(george());
		owners.save(george());
		owners.save(george());
		owners.save(george());
		owners.save(george());
		Page<Owner> result = owners.findByLastName("Franklin", Pageable.ofSize(5));
		assertThat(result.getContent()).hasSize(5);
		assertThat(result.getTotalElements()).isEqualTo(7);
	}

}
