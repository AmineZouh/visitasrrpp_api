package eby.py.visitasrrpp.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import eby.py.visitasrrpp.models.entity.Visitante;

public interface IVisitanteDao extends CrudRepository<Visitante, Long> {
	
	@EntityGraph(attributePaths = {"tipoDocumento","pais"})
	public List<Visitante> findAll();

	public List<Visitante> findByGroupId(Long groupId);

}
