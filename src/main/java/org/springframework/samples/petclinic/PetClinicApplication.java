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

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@EnableMapRepositories
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	public CommandLineRunner setup(KeyValueTemplate template, OwnerRepository owners, PetTypeRepository types) {
		return args -> {
			template.insert(1, vet(1, "James", "Carter"));
			template.insert(2, vet(2, "Helen", "Leary", "surgery"));
			template.insert(3, vet(3, "Linda", "Douglas", "surgery", "dentistry"));
			template.insert(4, vet(4, "Rafael", "Ortega", "surgery"));
			template.insert(5, vet(5, "Henry", "Stevens", "radiology"));
			template.insert(6, vet(6, "Sharon", "Jenkins"));
			template.insert(1, type(1, "cat"));
			template.insert(2, type(2, "dog"));
			template.insert(3, type(3, "lizard"));
			template.insert(4, type(4, "snake"));
			template.insert(5, type(5, "bird"));
			template.insert(6, type(6, "hamster"));
			Owner owner = owner(1, "George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
			owners.save(owner);
			owner.addPet(pet(1, "Leo", LocalDate.of(2010, 9, 7), owner, findPetType(types.findAll(), "cat")));
			owner = owner(2, "Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");
			owners.save(owner);
			owner.addPet(pet(2, "Basil", LocalDate.of(2012, 8, 6), owner, findPetType(types.findAll(), "hamster")));
			owner = owner(4, "Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198");
			owners.save(owner);
			owner.addPet(pet(4, "Jewel", LocalDate.of(2010, 3, 30), owner, findPetType(types.findAll(), "dog")));
			owner = owner(6, "Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654");
			owners.save(owner);
			owner.addPet(pet(7, "Samantha", LocalDate.of(2012, 9, 4), owner, findPetType(types.findAll(), "cat")));
			owner.getPet(7).addVisit(visit(1, LocalDate.of(2013, 1, 1), "rabies shot"));
		};
	}

	private Visit visit(Integer id, LocalDate date, String description) {
		Visit visit = new Visit();
		visit.setId(id);
		visit.setDate(date);
		visit.setDescription(description);
		return visit;
	}

	private Pet pet(Integer id, String name, LocalDate date, Owner owner, PetType findPetType) {
		Pet pet = new Pet();
		pet.setId(id);
		pet.setName(name);
		pet.setBirthDate(date);
		return pet;
	}

	private PetType findPetType(Collection<PetType> types, String name) {
		for (PetType petType : types) {
			if (name.equals(petType.getName())) {
				return petType;
			}
		}
		return null;
	}

	private PetType type(int id, String name) {
		PetType petType = new PetType();
		petType.setId(id);
		petType.setName(name);
		return petType;
	}

	private Vet vet(Integer id, String first, String last, String... speciality) {
		Vet vet = new Vet();
		vet.setId(id);
		vet.setFirstName(first);
		vet.setLastName(last);
		for (String name : speciality) {
			Specialty spec = new Specialty();
			spec.setName(name);
			vet.addSpecialty(spec);
		}
		return vet;
	}

	private Owner owner(Integer id, String first, String last, String address, String city, String telephone) {
		Owner owner = new Owner();
		owner.setId(id);
		owner.setFirstName(first);
		owner.setLastName(last);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		return owner;
	}

}
