package com.elipcero.masteringmvc;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

@Controller
public class PictureUploadController {
	
	public static final Resource PICTURES_DIR = new FileSystemResource("./pictures");
	
	private final Resource picturesDir;
	private final Resource anonymousPicture;
	
	@Autowired
	public PictureUploadController(PictureUploadProperties uploadProperties) {
		picturesDir = uploadProperties.getUploadPath();
		anonymousPicture = uploadProperties.getAnonymousPicture();
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
	
		return "uploadPage";
	}
	
	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}
	
	@RequestMapping(value = "/uploadedPicture")
	public void getUploadedPicture(
			HttpServletResponse response) throws	IOException {

		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(anonymousPicture.getFilename()));
		IOUtils.copy(anonymousPicture.getInputStream(), response.getOutputStream());
	}	
}