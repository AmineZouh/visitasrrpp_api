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
import eby.py.visitasrrpp.models.dto.PaisDto;
import eby.py.visitasrrpp.models.entity.Pais;
import eby.py.visitasrrpp.models.entity.PaisApi;
import eby.py.visitasrrpp.models.services.IPaisApiService;
import eby.py.visitasrrpp.models.services.IPaisService;

@RestController
@RequestMapping("/api/paises")
@CrossOrigin("*")
public class PaisRestController {
	
	@Autowired
	private IPaisService paisService;
	
	@Autowired
	private IPaisApiService apiPaisService;
	
	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllPaises() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<Pais> pais = paisService.findAll();
			List<PaisDto> dto = pais.stream().map(p -> paisDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el pais de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllPaisesFromApi() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<PaisApi> pais = apiPaisService.findAll();
			List<PaisDto> dto = pais.stream().map(p -> paisapiDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el pais de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@GetMapping("/{id}")
	public ResponseEntity<?> showPais(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Pais pais = paisService.findById(id);
			return ResponseEntity.ok(pais);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los paises de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun pais con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createPais(@RequestBody Pais pais) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Pais newPais = paisService.save(pais);

			PaisDto dtoCreated = paisDaoToDto(newPais);

			response.put("mensaje", "Registro creado con exito");
			response.put("Registro nuevo creado", dtoCreated);

			return new ResponseEntity<PaisDto>(dtoCreated, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updatePais(@RequestBody Pais pais, @RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Pais paisActual = paisService.findById(id);

			paisActual.setNombre(pais.getNombre());

			Pais paisUpdate = paisService.save(paisActual);
			PaisDto dtoUpdated = paisDaoToDto(paisUpdate);

			return new ResponseEntity<PaisDto>(dtoUpdated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun pais con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el pais con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePais(@RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			paisService.deleteById(id);

			response.put("mensaje", "Registro borrado");

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al eliminar registro con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {

			response.put("mensaje", "Registro con id: " + id + " no encontrado");

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	private PaisDto paisDaoToDto(Pais pais) {
		return mapper.map(pais, PaisDto.class);
	}
	
	private PaisDto paisapiDaoToDto(PaisApi paisapi) {
		return mapper.map(paisapi, PaisDto.class);
	}
}
