package eby.py.visitasrrpp.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.IPersonaDao;
import eby.py.visitasrrpp.models.dto.AuthenticationRequest;
import eby.py.visitasrrpp.models.entity.Persona;
import eby.py.visitasrrpp.util.AESencryption;

@Service
public class PersonaServiceImpl implements IPersonaService {

	@Autowired
	private IPersonaDao personaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {

		return (List<Persona>) personaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Persona findById(Long id) throws NotFoundException {

		return personaDao.findById(id).orElseThrow(() -> new NotFoundException());

	}

	@Override
	@Transactional
	public Persona save(Persona persona) throws Exception {
		persona.setPassword(AESencryption.encrypt(persona.getPassword()));
		return personaDao.save(persona);
	}

	@Override
	@Transactional
	public Persona update(Persona persona) {
		return personaDao.save(persona);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		personaDao.deleteById(id);

	}

	@Override
	public Persona login(AuthenticationRequest r) throws Exception {
		// TODO Auto-generated method stub
		Persona p = personaDao.findByEmail(r.getEmail());
		if(p!=null && p.getPassword().equals(AESencryption.encrypt(r.getPassword())))
			return p;
		return null;
	}

}
