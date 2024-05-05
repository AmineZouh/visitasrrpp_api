package eby.py.visitasrrpp.models.services;

import java.util.List;

import eby.py.visitasrrpp.models.entity.PaisApi;

public interface IPaisApiService {	
	 
	List<PaisApi> saveAll(List<PaisApi> paises);
	
	List<PaisApi> findAll();
	

}
