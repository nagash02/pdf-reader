package cl.compendium.pdf_reader.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfImageContentService {

	private static Logger logger = LogManager.getLogger(PdfImageContentService.class);

	@Autowired
	private HelperService helper;

	public String readFromPdfImage(MultipartFile file) throws IOException {
		StringBuilder bld = new StringBuilder();

		File sourceFile = helper.multipartToFile(file);

		List<String> imagenes = createImages(sourceFile);
		for (String img : imagenes) {
			bld.append(readTesseract(img));
		}
		return bld.toString();
	}

	private List<String> createImages(File sourceFile) throws IOException {

		List<String> salida = new ArrayList<>();

		String destinationDir = "/Users/cristian.gg/DocumentosPrueba/images/"; // converted images from pdf document are
																				// saved here

		File destinationFile = new File(destinationDir);
		if (!destinationFile.exists()) {
			destinationFile.mkdir();
			logger.info("Folder Created -> %s", destinationFile.getAbsolutePath());
		}

		PDDocument document = Loader.loadPDF(sourceFile);
		PDFRenderer pdfRenderer = new PDFRenderer(document);

		int numberOfPages = document.getNumberOfPages();

		String fileName = sourceFile.getName().replace(".pdf", "");
		String fileExtension = "png";
		/*
		 * 600 dpi give good image clarity but size of each image is 2x times of 300
		 * dpi. Ex: 1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB 2.
		 * For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
		 */
		int dpi = 600;// use less dpi for to save more space in harddisk. For professional usage you
						// can use more than 300dpi

		for (int i = 0; i < numberOfPages; ++i) {
			String fileImageName = destinationDir + fileName + "_" + (i + 1) + "." + fileExtension;
			salida.add(fileImageName);
			File outPutFile = new File(fileImageName);
			BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
			ImageIO.write(bImage, fileExtension, outPutFile);
		}

		document.close();
		return salida;
	}

	private String readTesseract(String imagePath) {
		try {
			execTessarct(imagePath);
			return readText(imagePath);
		} catch (Exception ex) {
			logger.error(ex);
			return "";
		}
	}

	private void execTessarct(String image) throws IOException {
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

		Process process;

		if (isWindows) {
			process = Runtime.getRuntime()
					.exec("cmd.exe /c tesseract -l eng /Users/cristian.gg/DocumentosPrueba/images/" + image
							+ " /Users/cristian.gg/DocumentosPrueba/images/text");
		} else {
			String comando = "/usr/local/Cellar/tesseract/4.1.1/bin/tesseract " + image + " "
					+ image.substring(0, image.length() - 4);
			logger.info(comando);

			process = Runtime.getRuntime().exec(comando);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			logger.info(line);
		}
	}

	private String readText(String image) throws IOException {
		StringBuilder bld = new StringBuilder();
		String fileName = image.substring(0, image.length() - 4) + ".txt";

		String line;
		FileReader f = new FileReader(fileName);
		try (BufferedReader b = new BufferedReader(f)) {

			while ((line = b.readLine()) != null) {
				bld.append(line);
			}

		} catch (Exception ex) {
			logger.error(ex);
		}
		return bld.toString();
	}
}
