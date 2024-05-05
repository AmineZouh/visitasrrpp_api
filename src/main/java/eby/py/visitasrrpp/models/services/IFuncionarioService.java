package eby.py.visitasrrpp.models.services;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import eby.py.visitasrrpp.models.entity.Funcionario;

public interface IFuncionarioService {

public List<Funcionario> findAll();
	
	public Funcionario findById(Long id) throws NotFoundException;
	
	public Funcionario save(Funcionario funcionario);	
	
	public void deleteById(Long id)throws NotFoundException;
}
