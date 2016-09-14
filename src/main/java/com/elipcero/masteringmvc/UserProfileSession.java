package com.elipcero.masteringmvc;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserProfileSession implements Serializable {
	
	private String twitterHandle;
	private String email;
	private LocalDate birthDate;
	private List<String> tastes = new ArrayList<>();
	
	//	Remember that all the properties of our session bean must be serializable, unlike
	//	resources. So we need to store it differently. The URL class seems to be a good fit. It
	//	is serializable and it is easy to create a resource from a URL with the UrlResource	class
	private URL picturePath;
	
	public void setPicturePath(Resource picturePath) throws	IOException {
		this.picturePath = picturePath.getURL();
	}
	
	public Resource getPicturePath() {
		return picturePath == null ? null : new	UrlResource(picturePath);
	}	
	
	public void saveForm(ProfileForm profileForm) {
		this.twitterHandle = profileForm.getTwitterHandle();
		this.email = profileForm.getEmail();
		this.birthDate = profileForm.getBirthDate();
		this.tastes = profileForm.getTastes();
	}
	
	public ProfileForm toForm() {
		ProfileForm profileForm = new ProfileForm();
		profileForm.setTwitterHandle(twitterHandle);
		profileForm.setEmail(email);
		profileForm.setBirthDate(birthDate);
		profileForm.setTastes(tastes);
		return profileForm;
	}

	public List<String> getTastes() {
		return this.tastes;
	}
}