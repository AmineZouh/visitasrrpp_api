package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.TipoDocumento;

public interface ITipoDocumentoService {
	
	public List<TipoDocumento> findAll();

	public TipoDocumento findById(Long id) throws NotFoundException;

	public TipoDocumento save(TipoDocumento tipoDocumento);

	public void deleteById(Long id) throws NotFoundException;

}
