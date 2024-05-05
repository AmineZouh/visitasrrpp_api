package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.Visita;

public interface IVisitaService {

	public List<Visita> findAll();

	public Visita findById(Long id) throws NotFoundException;

	public Visita save(Visita visita);

	public void deleteById(Long id) throws NotFoundException;

}
