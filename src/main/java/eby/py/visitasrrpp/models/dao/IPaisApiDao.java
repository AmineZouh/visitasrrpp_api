package eby.py.visitasrrpp.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eby.py.visitasrrpp.models.entity.PaisApi;

public interface IPaisApiDao extends CrudRepository<PaisApi, Long> {
	
	@Query("select p from PaisApi p order by p.iddTel")
	public List<PaisApi> findAll();

}
