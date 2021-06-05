package cl.compendium.pdf_reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cl.compendium.pdf_reader.config.GlobalConfiguration;
import cl.compendium.pdf_reader.config.SecurityConfiguration;
import cl.compendium.pdf_reader.config.SwaggerConfiguration;

@SpringBootApplication
public class PdfReaderApplication {
	
	private static Logger logger = LogManager.getLogger(PdfReaderApplication.class);

	public static void main(String[] args) {
		Class<?>[] configClasses = { GlobalConfiguration.class, SecurityConfiguration.class,
				SwaggerConfiguration.class };
		SpringApplication.run(configClasses, args);
		logger.info("UP AND RUNNING");
	}

}
