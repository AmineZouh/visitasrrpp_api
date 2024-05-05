package eby.py.visitasrrpp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eby.py.visitasrrpp.models.dto.TipoDocumentoDto;
import eby.py.visitasrrpp.models.entity.TipoDocumento;
import eby.py.visitasrrpp.models.services.ITipoDocumentoService;

@RestController
@RequestMapping("/api/tipos-documentos")
@CrossOrigin("*")
public class TipoDocumentoRestController {

	@Autowired
	private ITipoDocumentoService tipoDocumentoService;
	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllTipoDocumentos() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<TipoDocumento> tipoDocumento = tipoDocumentoService.findAll();
			List<TipoDocumentoDto> dto = tipoDocumento.stream().map(p -> tipoDocumentoDaoToDto(p))
					.collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el tipo de documento de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("{id}")
	public ResponseEntity<?> showTipoDocumento(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			TipoDocumento tipoDocumento = tipoDocumentoService.findById(id);
			return ResponseEntity.ok(tipoDocumento);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los tipo de documentos de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun tipo de documento con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createTipoDocumento(@RequestBody TipoDocumento tipoDocumento) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			TipoDocumento newTipoDocumento = tipoDocumentoService.save(tipoDocumento);

			TipoDocumentoDto dtoCreated = tipoDocumentoDaoToDto(newTipoDocumento);

			response.put("mensaje", "Registro creado con exito");
			response.put("Registro nuevo creado", dtoCreated);

			return new ResponseEntity<TipoDocumentoDto>(dtoCreated, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateTipoDocumento(@RequestBody TipoDocumento tipoDocumento, @RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			TipoDocumento tipoDocumentoActual = tipoDocumentoService.findById(id);

			tipoDocumentoActual.setNombre(tipoDocumento.getNombre());

			TipoDocumento tipoDocumentoUpdated = tipoDocumentoService.save(tipoDocumentoActual);
			TipoDocumentoDto dtoUpdated = tipoDocumentoDaoToDto(tipoDocumentoUpdated);

			return new ResponseEntity<TipoDocumentoDto>(dtoUpdated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun tipo de documento con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el tipo de documento con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTipoDocumento(@RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			tipoDocumentoService.deleteById(id);

			response.put("mensaje", "Registro borrado");

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al eliminar registro con id: " + id);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		  catch (NotFoundException e) {

			response.put("mensaje", "Registro con id: " + id + " no encontrado");

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	private TipoDocumentoDto tipoDocumentoDaoToDto(TipoDocumento tipoDocumento) {
		return mapper.map(tipoDocumento, TipoDocumentoDto.class);
	}

}
