package eby.py.visitasrrpp.models.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import eby.py.visitasrrpp.models.dao.IVisitanteDao;
import eby.py.visitasrrpp.models.dao.IVisitanteGroupDao;
import eby.py.visitasrrpp.models.entity.ImageSide;
import eby.py.visitasrrpp.models.entity.Visitante;
import eby.py.visitasrrpp.models.entity.VisitanteGroup;


@Service
public class VisitanteGroupServiceImpl implements IVisitanteGroupService{

    @Autowired
    private IVisitanteGroupDao repo;
    @Autowired
    private IVisitanteDao visitanteDao;

    @Override
    public List<VisitanteGroup> findAll() {
        return repo.findAll();
    }

    @Override
    public VisitanteGroup save(VisitanteGroup visitanteGroup) {
        // TODO Auto-generated method stub
        return repo.save(visitanteGroup);
    }

    @Override
    public VisitanteGroup findById(Long id) throws NotFoundException {
        // TODO Auto-generated method stub
        return repo.findById(id).get();
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        // TODO Auto-generated method stub
        List<Visitante> groupVisitantes = repo.findById(id).get().getVisitors();
        groupVisitantes.forEach(v -> v.setGroup(null));
        visitanteDao.saveAll(groupVisitantes);
        repo.deleteById(id);
    }


}
