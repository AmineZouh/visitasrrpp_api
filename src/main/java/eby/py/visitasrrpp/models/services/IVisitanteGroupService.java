package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.VisitanteGroup;

public interface IVisitanteGroupService {
    
    public List<VisitanteGroup> findAll();

    public  VisitanteGroup save(VisitanteGroup visitanteGroup);

    public VisitanteGroup findById(Long id) throws NotFoundException;

    public  void delete(Long id) throws NotFoundException;
}
