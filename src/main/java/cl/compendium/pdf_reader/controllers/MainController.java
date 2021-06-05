package cl.compendium.pdf_reader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cl.compendium.pdf_reader.service.PdfImageContentService;
import cl.compendium.pdf_reader.service.PdfReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController()
@Api(tags = "Ejemplo", value = "Ejemplo de tags para descipcion")
public class MainController {
	
	@Autowired
	private PdfReadService readService;
	
	@Autowired
	private PdfImageContentService pdfImageService;

	@ApiOperation(value = "Esto es un ejemplo de retorno de pojo desde un ser servicio", notes = "Trate de Entregar la mayor info posible")
    @ApiResponses({ @ApiResponse(code = 200, message = "Mensaje correcto", response = HttpStatus.class),
	    @ApiResponse(code = 400, message = "Esto es el resultado de una validación de negocio", response = HttpStatus.class),
	    @ApiResponse(code = 409, message = "Error en la aplicacion que fue capturada por un try", response = HttpStatus.class) })
    @GetMapping(value = "/v1/mensaje")
	public ResponseEntity<?> getHolaMundo() {
		return new ResponseEntity<>("hola mundo", HttpStatus.OK);
	}
	
	@ApiOperation(value = "Permite subir un archivo", 
			notes = "Sube un archivo")
	@ApiResponses({ 
		@ApiResponse(code = 201, message = "Archivo en el sistema de forma correcta", response = HttpStatus.class),
		@ApiResponse(code = 200, message = "Archivo en el sistema de forma correcta", response = HttpStatus.class),
		@ApiResponse(code = 400, message = "Fallo por validacion de negocio",response = HttpStatus.class),
		@ApiResponse(code = 409, message = "No se pudo subir el archivo",response = HttpStatus.class)
		})
	@RequestMapping(value = "/v1/archivos", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception{
		

		return new ResponseEntity<>(readService.readPdfText(file), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Permite subir un archivo", 
			notes = "Sube un archivo")
	@ApiResponses({ 
		@ApiResponse(code = 201, message = "Archivo en el sistema de forma correcta", response = HttpStatus.class),
		@ApiResponse(code = 200, message = "Archivo en el sistema de forma correcta", response = HttpStatus.class),
		@ApiResponse(code = 400, message = "Fallo por validacion de negocio",response = HttpStatus.class),
		@ApiResponse(code = 409, message = "No se pudo subir el archivo",response = HttpStatus.class)
		})
	@RequestMapping(value = "/v1/archivos-imagen", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<?> uploadFileImage(@RequestParam("file") MultipartFile file) throws Exception{
		

		return new ResponseEntity<>(pdfImageService.readFromPdfImage(file), HttpStatus.OK);
	}
}
