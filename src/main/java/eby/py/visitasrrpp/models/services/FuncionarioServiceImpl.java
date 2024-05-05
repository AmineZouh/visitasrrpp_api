package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eby.py.visitasrrpp.models.dao.IFuncionarioDao;
import eby.py.visitasrrpp.models.entity.Funcionario;

@Service
public class FuncionarioServiceImpl implements IFuncionarioService{

	@Autowired
	private IFuncionarioDao funcionarioDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Funcionario> findAll() {	
		
		return (List<Funcionario>) funcionarioDao.findAll();	
	}	
	
	@Override
	@Transactional(readOnly = true)
	public Funcionario findById(Long id) throws NotFoundException {

		return (Funcionario) funcionarioDao.findById(id).orElseThrow(() -> new NotFoundException());

	}

	@Override
	public Funcionario save(Funcionario funcionario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}
}
