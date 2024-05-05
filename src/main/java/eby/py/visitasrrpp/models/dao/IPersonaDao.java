package eby.py.visitasrrpp.models.dao;


import org.springframework.data.repository.CrudRepository;
import eby.py.visitasrrpp.models.entity.Persona;


public interface IPersonaDao extends CrudRepository<Persona, Long>{

    Persona findByEmail(String email);

}
