package eby.py.visitasrrpp.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import eby.py.visitasrrpp.models.entity.VisitanteGroup;

public interface IVisitanteGroupDao extends JpaRepository<VisitanteGroup, Long>{
    
}
