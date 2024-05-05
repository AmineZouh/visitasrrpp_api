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

import eby.py.visitasrrpp.models.dto.AuthenticationRequest;
import eby.py.visitasrrpp.models.dto.AuthenticationResponse;
import eby.py.visitasrrpp.models.dto.PersonaDto;
import eby.py.visitasrrpp.models.entity.Persona;
import eby.py.visitasrrpp.models.services.IPersonaService;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin("*")
public class PersonaRestController {

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllPersonas() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<Persona> personas = personaService.findAll();
			List<PersonaDto> dto = personas.stream().map(p -> personaDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener las personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showPersona(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Persona persona = personaService.findById(id);
			return ResponseEntity.ok(persona);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener las personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createPersona(@RequestBody Persona persona) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Persona newPersona = personaService.save(persona);

			PersonaDto dtoCreated = personaDaoToDto(newPersona);

			response.put("mensaje", "Registro creado con exito");
			response.put("Registro nuevo creado", dtoCreated);

			return new ResponseEntity<PersonaDto>(dtoCreated, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {

			response.put("mensaje", "Failure while enrypting password");
			response.put("Error", "Encryption error");

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updatePersona(@RequestBody Persona persona, @PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Persona personaActual = personaService.findById(id);

			personaActual.setApellido(persona.getApellido());
			personaActual.setNombre(persona.getNombre());
			personaActual.setTelefono(persona.getTelefono());
			personaActual.setEmail(persona.getEmail());
			personaActual.setTipoDocumento(persona.getTipoDocumento());
			personaActual.setPais(persona.getPais());
			personaActual.setSexo(persona.getSexo());
			personaActual.setCodigoTelefono(persona.getCodigoTelefono());

			Persona personaUpdated = personaService.update(personaActual);
			PersonaDto dtoUpdated = personaDaoToDto(personaUpdated);

			return new ResponseEntity<PersonaDto>(dtoUpdated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener la persona con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		Persona p = personaService.login(request);
		if (p != null)
			return new ResponseEntity<AuthenticationResponse>(personaDaoToAuthResponse(p), HttpStatus.ACCEPTED);
		response.put("mensaje", "Email or passord is not correct ");
		response.put("error", "Authentication failure");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePersona(@RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			personaService.deleteById(id);

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

	private PersonaDto personaDaoToDto(Persona persona) {
		return mapper.map(persona, PersonaDto.class);
	}

	private AuthenticationResponse personaDaoToAuthResponse(Persona persona) {
		return mapper.map(persona, AuthenticationResponse.class);
	}
}
