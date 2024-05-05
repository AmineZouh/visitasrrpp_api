package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.Pais;

public interface IPaisService {

	public List<Pais> findAll();

	public Pais findById(Long id) throws NotFoundException;

	public Pais save(Pais pais);

	public void deleteById(Long id) throws NotFoundException;

}
