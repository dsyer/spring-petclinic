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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping(headers = "HX-Request=true")
class OwnerHtmxController {

	private final OwnerController delegate;

	public OwnerHtmxController(OwnerController delegate) {
		this.delegate = delegate;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		delegate.setAllowedFields(dataBinder);
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		delegate.initCreationForm(model);
		return "owners/createOrUpdateOwnerForm :: form(action='true')";
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, Map<String, Object> model) {
		String view = delegate.processCreationForm(owner, result);
		if (view.startsWith("redirect")) {
			return view;
		}
		return "owners/createOrUpdateOwnerForm :: form('true')";
	}

	@GetMapping("/owners/find")
	public String initFindFormFragments(Map<String, Object> model) {
		delegate.initFindForm(model);
		return "owners/findOwners :: partials";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model) {
		String view = delegate.processFindForm(page, owner, result, model);
		if (view.startsWith("redirect:")) {
			return view;
		}
		if (view.equals("owners/findOwners")) {
			return view + " :: form(action='true')";
		}
		return view + " :: list(action='true')";
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		String view = delegate.initUpdateOwnerForm(ownerId, model);
		return view + " :: form(action='true')";
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		String view = delegate.processUpdateOwnerForm(owner, result, ownerId);
		if (view.startsWith("redirect:")) {
			return view;
		}
		return view + " :: form(action='true')";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = delegate.showOwner(ownerId);
		mav.setViewName(mav.getViewName() + " :: owner(action='true')");
		return mav;
	}

}
