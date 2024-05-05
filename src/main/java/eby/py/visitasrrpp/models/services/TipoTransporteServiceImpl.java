package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.ITipoTransporteDao;
import eby.py.visitasrrpp.models.entity.TipoTransporte;

@Service
public class TipoTransporteServiceImpl implements ITipoTransporteService {
	
	@Autowired
	private ITipoTransporteDao tipoTransporteDao;

	@Override
	@Transactional(readOnly = true)
	public List<TipoTransporte> findAll() {

		return (List<TipoTransporte>) tipoTransporteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoTransporte findById(Long id) throws NotFoundException {

		return tipoTransporteDao.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Override
	@Transactional
	public TipoTransporte save(TipoTransporte tipoTransporte) {

		return tipoTransporteDao.save(tipoTransporte);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		tipoTransporteDao.deleteById(id);

	}

}
