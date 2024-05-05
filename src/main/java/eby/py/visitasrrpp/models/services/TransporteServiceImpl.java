package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.ITransporteDao;
import eby.py.visitasrrpp.models.entity.Transporte;

@Service
public class TransporteServiceImpl implements ITransporteService {

	@Autowired
	private ITransporteDao transporteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Transporte> findAll() {

		return (List<Transporte>) transporteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Transporte findById(Long id) throws NotFoundException {

		return transporteDao.findById(id).orElseThrow(() -> new NotFoundException());

	}

	@Override
	@Transactional
	public Transporte save(Transporte transporte) {

		return transporteDao.save(transporte);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		transporteDao.deleteById(id);

	}

}
