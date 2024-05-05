package eby.py.visitasrrpp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import eby.py.visitasrrpp.models.entity.VisitanteGroup;
import eby.py.visitasrrpp.models.services.VisitanteGroupServiceImpl;

@RestController
@RequestMapping("/api/groupes")
@CrossOrigin("*")
public class VisitanteGroupRestController {

    @Autowired
    private  VisitanteGroupServiceImpl service;

    @GetMapping("/")
    public ResponseEntity<?> findAll(){
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            return ResponseEntity.ok(service.findAll());
            
        } catch (DataAccessException e) {

			response.put("mensaje", "Failure while retieving data");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody VisitanteGroup group){
		Map<String, Object> response = new HashMap<String, Object>();
        try {
			return new ResponseEntity<VisitanteGroup>(service.save(group), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            // TODO: handle exception
            response.put("mensaje", "Failure while saving data");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody VisitanteGroup group){
		Map<String, Object> response = new HashMap<String, Object>();
        try {
			return new ResponseEntity<VisitanteGroup>(service.save(group), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            // TODO: handle exception
            response.put("mensaje", "Failure while updating data");
			response.put("Error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById( @PathVariable("id") Long id ){
        Map<String, Object> response = new HashMap<String, Object>();
        try{
            return ResponseEntity.ok(service.findById(id));
        }catch (DataAccessException e) {

			response.put("mensaje", "Failure to retrieve  the data");
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NotFoundException e) {

			response.put("mensaje", "No record with this id found, id:" + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById( @PathVariable("id") Long id ) {
        
        Map<String, Object> response = new HashMap<String, Object>();
		try {

			service.delete(id);

			response.put("mensaje", "Record deleted");

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (DataAccessException e) {

			response.put("mensaje", "Failure while deleting record with id: " + id);
			response.put("error", e.getMostSpecificCause().getMessage());

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {

			response.put("mensaje", "Record with id: " + id + " not found");			

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
    }

 
}
