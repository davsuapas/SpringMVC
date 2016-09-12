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
	
	@Autowired
	public PictureUploadController(PictureUploadProperties uploadProperties, MessageSource messageSource) {
		picturesDir = uploadProperties.getUploadPath();
		anonymousPicture = uploadProperties.getAnonymousPicture();
		this.messageSource = messageSource;
	}	
	
	// Cuando se hace model.addAttribute("picturePath", new FileSystemResource(tempFile));
	// el valor por defecto que se configuro en el constructor, es modificado con el valor
	// que se a�ade con addAttribute, y se propaga a trav�s de los diferentes request porque
	// se graba por sessi�n con @SessionAttributes("picturePath")
	@ModelAttribute("picturePath")
	public Resource picturePath() {
		return anonymousPicture;
	}	
	
	@RequestMapping("upload")
	public String uploadPage() {
		return "uploadPage";
	}
	
	// Maneja todas las expcepciones de este controlador para el tipo ioexception
	@ExceptionHandler(IOException.class)
	public ModelAndView handleIOException(Locale locale) {
		ModelAndView modelAndView = new ModelAndView("uploadPage");
		modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));
		return modelAndView;
	}
	
	// Este método se lanza cuando existe ua excepción de tipo MultipartException. Se redirije en WebConfiguration
	@RequestMapping("uploadError")
	public ModelAndView onUploadError(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView("uploadPage");
	
		modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
		
		return modelAndView;
	}	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String onUpload(MultipartFile file, Model model) throws IOException {
				
		String fileExtension = getFileExtension(file.getOriginalFilename());
		
		File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());
		
		try (InputStream in = file.getInputStream();
				OutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}

		model.addAttribute("picturePath", new FileSystemResource(tempFile));
		
		return "uploadPage";		

	}
	
	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}
	
	@RequestMapping(value = "/uploadedPicture")
	public void getUploadedPicture(
			HttpServletResponse response,
			@ModelAttribute("picturePath") Resource picturePath) throws	IOException {
		
		Path path = picturePath.getFile().toPath();
		
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(path.toString()));
		Files.copy(path, response.getOutputStream());
	}	
}