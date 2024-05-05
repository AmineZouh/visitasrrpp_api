package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.IVisitaDao;
import eby.py.visitasrrpp.models.entity.Visita;

@Service
public class VisitaServiceImpl implements IVisitaService {

	@Autowired
	private IVisitaDao visitaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Visita> findAll() {

		return (List<Visita>) visitaDao.findAll();
	}

	@Override
	@Transactional
	public Visita findById(Long id) throws NotFoundException {

		return visitaDao.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Override
	@Transactional
	public Visita save(Visita visita) {

		return visitaDao.save(visita);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		visitaDao.deleteById(id);

	}
}
