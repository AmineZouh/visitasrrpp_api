package eby.py.visitasrrpp.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import eby.py.visitasrrpp.models.entity.Funcionario;

public interface IFuncionarioDao extends CrudRepository<Funcionario, Long> {

	@EntityGraph(attributePaths = { "tipoDocumento", "pais" })
	public List<Funcionario> findAll();

}
