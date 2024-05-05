package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.TipoTransporte;

public interface ITipoTransporteService {

	public List<TipoTransporte> findAll();

	public TipoTransporte findById(Long id) throws NotFoundException;

	public TipoTransporte save(TipoTransporte tipoTransporte);

	public void deleteById(Long id) throws NotFoundException;

}
