package cl.compendium.pdf_reader.service;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HelperService {
	
	private static Logger logger = LogManager.getLogger(HelperService.class);
	
	@SuppressWarnings("finally")
	

	public File multipartToFile(MultipartFile file) {
		File document =  null;
		try {
			String fileName = file.getOriginalFilename();
			String prefix = fileName!=null ? fileName.substring(fileName.lastIndexOf(".")) : "";
			document = File.createTempFile(fileName, prefix);
		    file.transferTo(document);
		}catch(Exception ex) {
			logger.error("Error al procesar multipart a archivo");
		}finally {
			return document;
		}
	}
}
