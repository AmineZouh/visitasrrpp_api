package eby.py.visitasrrpp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eby.py.visitasrrpp.models.dto.PaisApiDto;
import eby.py.visitasrrpp.models.entity.PaisApi;
import eby.py.visitasrrpp.models.services.IPaisApiService;

@RestController
@RequestMapping("/api-pais")
public class PaisApiRestController {

	@Autowired
	private IPaisApiService service;

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createPaisApi(@RequestBody List<PaisApiDto> paises) {

		Map<String, Object> response = new HashMap<String, Object>();

		try {

			List<PaisApi> paisesEntity = new ArrayList<PaisApi>();

			for (PaisApiDto paisApiDto : paises) {
				
				PaisApi newPais = new PaisApi();
				newPais.setNombre(paisApiDto.getName().getCommon());
				newPais.setCodigo(paisApiDto.getCca3());
				newPais.setBandera(paisApiDto.getFlags().getSVG());
				
				if (paisApiDto.getIdd().getSuffixes().length > 1 || paisApiDto.getIdd().getSuffixes().length == 0) {
					
					newPais.setIddTel(paisApiDto.getIdd().getRoot());
					
				} else {

					newPais.setIddTel(paisApiDto.getIdd().getRoot().concat(paisApiDto.getIdd().getSuffixes()[0]));

				}
				
				paisesEntity.add(newPais);

			}
			
			service.saveAll(paisesEntity);
			
			response.put("mensaje", "Registro creado con exito");

			return new ResponseEntity<List<PaisApi>>(paisesEntity, HttpStatus.CREATED);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al insertar el registro en la Base de Datos");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
