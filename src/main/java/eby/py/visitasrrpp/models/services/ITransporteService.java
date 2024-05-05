package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.Transporte;

public interface ITransporteService {

	public List<Transporte> findAll();

	public Transporte findById(Long id) throws NotFoundException;

	public Transporte save(Transporte transporte);

	// @Transactional
	public void deleteById(Long id) throws NotFoundException;
}
