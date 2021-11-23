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

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final OwnerRepository owners;

	private final VisitRepository visits;

	public OwnerController(OwnerRepository clinicService, VisitRepository visits) {
		this.owners = clinicService;
		this.visits = visits;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(path = "/owners/new", headers = "HX-Request=true")
	public String initCreationFormFragments(Map<String, Object> model) {
		initCreationForm(model);
		return "owners/createOrUpdateOwnerForm :: form(action='true')";
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			this.owners.save(owner);
			return "redirect:/owners/" + owner.getId();
		}
	}

	@PostMapping(path = "/owners/new", headers = "HX-Request=true")
	public String processCreationFormFragments(@Valid Owner owner, BindingResult result, Map<String, Object> model) {
		String view = processCreationForm(owner, result);
		if (view.startsWith("redirect")) {
			return "owners/createOrUpdateOwnerForm :: form('true')";
		}
		ModelAndView mav = showOwner(owner.getId());
		model.putAll(mav.getModel());
		return "owners/OwnerDetails :: owner(action='true')";
	}

	@GetMapping("/owners/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("owner", new Owner());
		return "owners/findOwners";
	}

	@GetMapping(path = "/owners/find", headers = "HX-Request=true")
	public String initFindFormFragments(Map<String, Object> model) {
		initFindForm(model);
		return "owners/findOwners :: partials";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		String lastName = owner.getLastName();
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, lastName);
		if (ownersResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		} else if (ownersResults.getTotalElements() == 1) {
			// 1 owner found
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		} else {
			// multiple owners found
			lastName = owner.getLastName();
			return addPaginationModel(page, model, lastName, ownersResults);
		}
	}

	@GetMapping(path = "/owners", headers = "HX-Request=true")
	public String processFindFormFragments(@RequestParam(defaultValue = "1") int page, Owner owner,
			BindingResult result, Model model) {
		String view = processFindForm(page, owner, result, model);
		if (view.equals("owners/findOwners")) {
			view = view + " :: form(action='true')";
		} else if (view.startsWith("redirect:")) {
			return view;
		}
		return view + " :: list(action='true')";
	}

	private String addPaginationModel(int page, Model model, String lastName, Page<Owner> paginated) {
		model.addAttribute("listOwners", paginated);
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {

		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);

	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.owners.findById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@GetMapping(path = "/owners/{ownerId}/edit", headers = "HX-Request=true")
	public String initUpdateOwnerFormFragments(@PathVariable("ownerId") int ownerId, Model model) {
		String view = initUpdateOwnerForm(ownerId, model);
		return view + " :: form(action='true')";
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			owner.setId(ownerId);
			this.owners.save(owner);
			return "redirect:/owners/{ownerId}";
		}
	}

	@PostMapping(path = "/owners/{ownerId}/edit", headers = "HX-Request=true")
	public String processUpdateOwnerFormFragments(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId, Map<String, Object> model) {
		String view = processUpdateOwnerForm(owner, result, ownerId);
		if (view.startsWith("redirect:")) {
			return view;
		}
		return view + " :: form(action='true')";
	}

	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner owner = this.owners.findById(ownerId);
		for (Pet pet : owner.getPets()) {
			pet.setVisitsInternal(visits.findByPetId(pet.getId()));
		}
		mav.addObject(owner);
		return mav;
	}

	@GetMapping(path = "/owners/{ownerId}", headers = "HX-Request=true")
	public ModelAndView showOwnerFragments(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = showOwner(ownerId);
		mav.setViewName(mav.getViewName() + " :: owner(action='true')");
		return mav;
	}
}
