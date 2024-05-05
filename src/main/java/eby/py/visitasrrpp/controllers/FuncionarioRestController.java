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

import eby.py.visitasrrpp.models.dto.FuncionarioDto;
import eby.py.visitasrrpp.models.entity.Funcionario;
import eby.py.visitasrrpp.models.services.IFuncionarioService;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin("*")
public class FuncionarioRestController {

	@Autowired
	private IFuncionarioService funcionarioService;

	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllFuncionarios() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<Funcionario> funcionario = funcionarioService.findAll();
			List<FuncionarioDto> dto = funcionario.stream().map(p -> funcionarioDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los Funcionarios de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> showFuncionario(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Funcionario funcionario = funcionarioService.findById(id);
			FuncionarioDto dto = funcionarioDaoToDto(funcionario);
			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los Funcionarios de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna funcionario con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createFuncionarios(@RequestBody Funcionario funcionario) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Funcionario newFuncionario = funcionarioService.save(funcionario);

			FuncionarioDto dtoCreated = funcionarioDaoToDto(newFuncionario);

			response.put("mensaje", "Registro creado con exito");
			response.put("Registro nuevo creado", dtoCreated);

			return new ResponseEntity<FuncionarioDto>(dtoCreated, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateFuncioanrios(@RequestBody Funcionario funcionario, @RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Funcionario funcionarioActual = funcionarioService.findById(id);

			funcionarioActual.setApellido(funcionario.getApellido());
			funcionarioActual.setNombre(funcionario.getNombre());
			funcionarioActual.setNroDocumento(funcionario.getNroDocumento());
			funcionarioActual.setTelefono(funcionario.getTelefono());
			funcionarioActual.setEmail(funcionario.getEmail());
			funcionarioActual.setTipoDocumento(funcionario.getTipoDocumento());
			funcionarioActual.setPais(funcionario.getPais());
			funcionarioActual.setSexo(funcionario.getSexo());
			funcionarioActual.setCodigoTelefono(funcionario.getCodigoTelefono());
			funcionarioActual.setLegajo(funcionario.getLegajo());
			funcionarioActual.setSede(funcionario.getSede());

			Funcionario visitanteUpdated = funcionarioService.save(funcionarioActual);
			FuncionarioDto dtoUpdated = funcionarioDaoToDto(visitanteUpdated);

			return new ResponseEntity<FuncionarioDto>(dtoUpdated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener la persona con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteFuncionario(@RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			funcionarioService.deleteById(id);

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

	private FuncionarioDto funcionarioDaoToDto(Funcionario funcionario) {
		return mapper.map(funcionario, FuncionarioDto.class);
	}
}
