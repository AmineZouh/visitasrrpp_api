package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.IPaisApiDao;
import eby.py.visitasrrpp.models.entity.PaisApi;

@Service
public class PaisApiServiceImpl implements IPaisApiService {

	@Autowired
	private IPaisApiDao paisApiDao;

	@Override
	@Transactional
	public List<PaisApi> saveAll(List<PaisApi> paises) {

		return (List<PaisApi>) paisApiDao.saveAll(paises);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaisApi> findAll() {

		return (List<PaisApi>) paisApiDao.findAll();
	}

}
