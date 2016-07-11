package com.elipcero.masteringmvc;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
	
	public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");
	
	private final Resource picturesDir;
	private final Resource anonymousPicture;
	
	@Autowired
	public PictureUploadController(PictureUploadProperties uploadProperties) {
		picturesDir = uploadProperties.getUploadPath();
		anonymousPicture = uploadProperties.getAnonymousPicture();
	}	
	
	// Cuando se hace model.addAttribute("picturePath", new FileSystemResource(tempFile));
	// el valor por defecto que se configuro en el constructor, es modificado con el valor
	// que se añade con addAttribute, y se propaga a través de los diferentes request porque
	// se graba por sessión con @SessionAttributes("picturePath")
	@ModelAttribute("picturePath")
	public Resource picturePath() {
		return anonymousPicture;
	}	
	
	@RequestMapping("upload")
	public String uploadPage() {
		return "uploadPage";
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