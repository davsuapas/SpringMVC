package com.elipcero.masteringmvc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {
	
	@RequestMapping("/profile")
	public String displayProfile(ProfileForm profileForm) {
		return "profilePage";
	}
	
	@RequestMapping(value = "/profile", params = {"save"}, method = RequestMethod.POST)
	public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "profilePage";
		}
		
		System.out.println("save ok" + profileForm);
		return "redirect:/profile";
	}
	
	@RequestMapping(value = "/profile", params = {"addTaste"})
	public String addRow(ProfileForm profileForm) {
		profileForm.getTastes().add(null);
		return "profilePage";
	}
	
	@RequestMapping(value = "/profile", params = {"removeTaste"})
	public String removeRow(ProfileForm profileForm, HttpServletRequest req) {
		Integer rowId = Integer.valueOf(req.getParameter("removeTaste"));
		profileForm.getTastes().remove(rowId.intValue());
		return "profilePage";
	}	
}