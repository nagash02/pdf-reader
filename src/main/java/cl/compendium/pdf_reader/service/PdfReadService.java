package cl.compendium.pdf_reader.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfReadService {

	private static Logger logger = LogManager.getLogger(PdfReadService.class);

	@Autowired
	private HelperService helper;

	public String readPdfText(MultipartFile file) throws IOException {
		File doc = null;

		try {
			doc = helper.multipartToFile(file);

			PDDocument document = Loader.loadPDF(doc);
			AccessPermission ap = document.getCurrentAccessPermission();
			if (!ap.canExtractContent()) {
				throw new IOException("You do not have permission to extract text");
			}
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		} finally {
			if (doc != null) {
				File f = new File(doc.toURI());
				Files.delete(f.toPath());
			}

		}
	}

}
