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

import eby.py.visitasrrpp.models.dto.TransporteDto;
import eby.py.visitasrrpp.models.entity.Transporte;
import eby.py.visitasrrpp.models.services.ITransporteService;

@RestController
@RequestMapping("/api/transportes")
@CrossOrigin("*")
public class TransporteRestController {

	@Autowired
	private ITransporteService transporteService;

	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllTransportes() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<Transporte> transporte = transporteService.findAll();
			List<TransporteDto> dto = transporte.stream().map(p -> transporteDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los transportes de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showTransporte(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Transporte transporte = transporteService.findById(id);
			TransporteDto dto = transporteDaoToDto(transporte);
			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener los transportes de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun transporte con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createTransporte(@RequestBody Transporte transporte) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Transporte newTransporte = transporteService.save(transporte);

			TransporteDto dtoCreated = transporteDaoToDto(newTransporte);

			response.put("mensaje", "Registro creado con exito");
			response.put("Registro nuevo creado", dtoCreated);

			return new ResponseEntity<TransporteDto>(dtoCreated, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateVisitante(@RequestBody Transporte transporte, @RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Transporte transporteActual = transporteService.findById(id);

			transporteActual.setNroChapa(transporte.getNroChapa());
			transporteActual.setTipoTransporte(transporte.getTipoTransporte());

			Transporte transporteUpdated = transporteService.save(transporteActual);
			TransporteDto dtoUpdated = transporteDaoToDto(transporteUpdated);

			return new ResponseEntity<TransporteDto>(dtoUpdated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ningun transporte con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener el transporte con id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTransporte(@RequestParam Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			transporteService.deleteById(id);

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

	private TransporteDto transporteDaoToDto(Transporte transporte) {
		return mapper.map(transporte, TransporteDto.class);
	}
}
