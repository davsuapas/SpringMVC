package com.elipcero.masteringmvc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {
	
	private UserProfileSession userProfileSession;
	
	@Autowired
	public ProfileController(UserProfileSession userProfileSession) {
		this.userProfileSession = userProfileSession;
	}
	
	// Elimino displayProfile(ProfileForm profileForm) porque ya lo tengo como model 
	// atribbute. mirar el método de abajp
	@RequestMapping("/profile")
	public String displayProfile() {
		return "profilePage";
	}
	
	@ModelAttribute
	public ProfileForm getProfileForm() {
		return userProfileSession.toForm();
	}	
	
	@RequestMapping(value = "/profile", params = {"save"}, method = RequestMethod.POST)
	public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "profilePage";
		}
		
		System.out.println("save ok" + profileForm);
		
		userProfileSession.saveForm(profileForm);
		
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