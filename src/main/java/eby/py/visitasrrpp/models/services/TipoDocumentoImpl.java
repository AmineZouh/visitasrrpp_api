package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.ITipoDocumentoDao;
import eby.py.visitasrrpp.models.entity.TipoDocumento;

@Service
public class TipoDocumentoImpl implements ITipoDocumentoService {
	
	@Autowired
	private ITipoDocumentoDao tipoDocumentoDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoDocumento> findAll() {
		
		return (List<TipoDocumento>)tipoDocumentoDao.findAll();
	}


	@Override
	@Transactional
	public TipoDocumento findById(Long id) throws NotFoundException {
		
		return tipoDocumentoDao.findById(id).orElseThrow(() -> new NotFoundException());
	}


	@Override
	@Transactional
	public TipoDocumento save(TipoDocumento tipoDocumento) {
		
		return tipoDocumentoDao.save(tipoDocumento);
	}


	@Override	
	@Transactional
	public void deleteById(Long id) throws NotFoundException {
		
		findById(id);
		tipoDocumentoDao.deleteById(id);
		
	}

}
