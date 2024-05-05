package eby.py.visitasrrpp.models.services;


import java.util.List;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
//import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dto.AuthenticationRequest;
import eby.py.visitasrrpp.models.entity.Persona;


public interface IPersonaService {
	public List<Persona> findAll();
	
	public Persona findById(Long id) throws NotFoundException;
	
	public Persona save(Persona persona) throws Exception;	
	
	public Persona update(Persona persona);	
	
	public Persona login(AuthenticationRequest r) throws Exception;
	//@Transactional
	public void deleteById(Long id)throws NotFoundException;
	
}
