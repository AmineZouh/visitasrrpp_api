package eby.py.visitasrrpp.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eby.py.visitasrrpp.models.dto.GroupeDocumentDto;
import eby.py.visitasrrpp.models.dto.VisitanteDto;
import eby.py.visitasrrpp.models.dto.VisitanteSaveRequest;
import eby.py.visitasrrpp.models.dto.VisitanteWithGroupeSaveRequest;
import eby.py.visitasrrpp.models.entity.ImageSide;
import eby.py.visitasrrpp.models.entity.Visitante;
import eby.py.visitasrrpp.models.services.IVisitanteService;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/visitantes")
@CrossOrigin("*")
public class VisitanteRestController {

	@Autowired
	private IVisitanteService visitanteService;

	@Autowired
	private ModelMapper mapper;

	@GetMapping("/list")
	public ResponseEntity<?> getAllVisitante() {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<Visitante> visitante = visitanteService.findAll();
			List<VisitanteDto> dto = visitante.stream().map(p -> visitanteDaoToDto(p)).collect(Collectors.toList());

			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener las personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/findByGroup")
	public ResponseEntity<?> findByGroup(@RequestParam Long groupId) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			return ResponseEntity.ok(visitanteService.findByGroup(groupId));
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al obtener las group personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/groupe-visitors/group-doc/{groupeId}")
	public ResponseEntity<GroupeDocumentDto> generateGroupeDoc(@PathVariable Long groupeId)
			throws JRException, IOException {
		return ResponseEntity.ok(
				GroupeDocumentDto
						.builder()
						.docData(Base64.getEncoder()
								.encodeToString(visitanteService.generateGroupeReport(groupeId).getBody()))
						.build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showVisitante(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Visitante visitante = visitanteService.findById(id);
			VisitanteDto dto = visitanteDaoToDto(visitante);
			return ResponseEntity.ok(dto);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener las personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/{id}/including-iddocs")
	public ResponseEntity<?> showVisitanteWithIDDocs(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			Visitante visitante = visitanteService.findById(id);
			// VisitanteDto dto = visitanteDaoToDto(visitante);
			return ResponseEntity.ok(visitante);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener las personas de la base de datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/new/byadmin")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createVisitanteByAdmin(@ModelAttribute VisitanteWithGroupeSaveRequest visitante) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			VisitanteDto newVisitante = visitanteService.saveByAdmin(visitante);

			return new ResponseEntity<VisitanteDto>(newVisitante, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createVisitante(@ModelAttribute VisitanteSaveRequest visitante) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			VisitanteDto newVisitante = visitanteService.saveWithDocs(visitante);

			return new ResponseEntity<VisitanteDto>(newVisitante, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/new-with-group")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createVisitanteWithGroupe(@ModelAttribute VisitanteWithGroupeSaveRequest visitante) throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			VisitanteDto newVisitante = visitanteService.saveWithGroupeByAdmin(visitante);

			return new ResponseEntity<VisitanteDto>(newVisitante, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("Error", e.getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateVisitante(@ModelAttribute VisitanteWithGroupeSaveRequest visitante)
			throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			VisitanteDto updated = visitanteService.updateWithDocs(visitante);

			return new ResponseEntity<VisitanteDto>(updated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + visitante.getPersonaId());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener la persona con id: " + visitante.getPersonaId());
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}


	@PutMapping("/update-with-groupe")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateVisitanteWithGroupe(@ModelAttribute VisitanteWithGroupeSaveRequest visitante)
			throws Exception {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			VisitanteDto updated = visitanteService.updateWithGroupeAndDocs(visitante);

			return new ResponseEntity<VisitanteDto>(updated, HttpStatus.CREATED);

		} catch (NotFoundException e) {

			response.put("mensaje", "No se encontró ninguna persona con el id " + visitante.getPersonaId());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al obtener la persona con id: " + visitante.getPersonaId());
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PutMapping("/addToGroup")
	public ResponseEntity<?> addUserToGroup(@RequestParam Long groupId, @RequestParam Long userId) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			Visitante visitante = visitanteService.addToGroup(userId, groupId);
			return ResponseEntity.ok(visitante);

		} catch (NoSuchElementException e) {

			response.put("mensaje", "Group or user not found ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {

			response.put("mensaje", "Failure retreiving user or group ");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/files/id-document-side-image/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
		ImageSide imageSide = visitanteService.getImageSide(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageSide.getName() + "\"")
				.body(imageSide.getData());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteVisitante(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			visitanteService.deleteById(id);

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

	private VisitanteDto visitanteDaoToDto(Visitante visitante) {
		return mapper.map(visitante, VisitanteDto.class);
	}

}