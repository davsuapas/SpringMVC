package com.elipcero.masteringmvc;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
	
	public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");
	
	private final Resource picturesDir;
	private final Resource anonymousPicture;
	
	// Multilenguaje
	private final MessageSource messageSource; 
	
	private final UserProfileSession userProfileSession;
	
	@Autowired
	public PictureUploadController(PictureUploadProperties uploadProperties, MessageSource messageSource, UserProfileSession userProfileSession) {
		picturesDir = uploadProperties.getUploadPath();
		anonymousPicture = uploadProperties.getAnonymousPicture();
		this.messageSource = messageSource;
		this.userProfileSession = userProfileSession;
	}	
	
	// Maneja todas las expcepciones de este controlador para el tipo ioexception
	@ExceptionHandler(IOException.class)
	public ModelAndView handleIOException(Locale locale) {
		
		ModelAndView modelAndView = new ModelAndView("profilePage");
		
		modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));
		modelAndView.addObject("profileForm", userProfileSession.toForm());
		
		return modelAndView;
	}
	
	// Este método se lanza cuando existe ua excepción de tipo MultipartException. Se redirije en WebConfiguration
	@RequestMapping("uploadError")
	public ModelAndView onUploadError(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView("uploadPage");
	
		modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
		modelAndView.addObject("profileForm", userProfileSession.toForm());
		
		return modelAndView;
	}	
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String onUpload(MultipartFile file, Model model) throws IOException {
				
		String fileExtension = getFileExtension(file.getOriginalFilename());
		
		File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
		
		try (InputStream in = file.getInputStream();
				OutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}

		userProfileSession.setPicturePath(new FileSystemResource(tempFile));
		
		return "redirect:profile";		

	}
	
	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}
	
	@RequestMapping(value = "/uploadedPicture")
	public void getUploadedPicture(HttpServletResponse response) throws	IOException {
		
		Resource picturePath = userProfileSession.getPicturePath();
		
		if (picturePath == null) {
			picturePath = anonymousPicture;
		}
		
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
		IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
	}	
}