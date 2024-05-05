package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.IPaisDao;
import eby.py.visitasrrpp.models.entity.Pais;

@Service
public class PaisServiceImpl implements IPaisService {

	@Autowired
	private IPaisDao paisDao;

	@Override
	@Transactional(readOnly = true)
	public List<Pais> findAll() {

		return (List<Pais>) paisDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Pais findById(Long id) throws NotFoundException {

		return paisDao.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Override
	@Transactional
	public Pais save(Pais pais) {

		return paisDao.save(pais);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		paisDao.deleteById(id);

	}

}
