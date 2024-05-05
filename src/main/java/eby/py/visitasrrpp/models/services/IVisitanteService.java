package eby.py.visitasrrpp.models.services;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

import eby.py.visitasrrpp.models.dto.VisitanteDto;
import eby.py.visitasrrpp.models.dto.VisitanteSaveRequest;
import eby.py.visitasrrpp.models.dto.VisitanteWithGroupeSaveRequest;
import eby.py.visitasrrpp.models.entity.ImageSide;
import eby.py.visitasrrpp.models.entity.Visitante;
import net.sf.jasperreports.engine.JRException;

public interface IVisitanteService {

	public List<Visitante> findAll();

	public Visitante findById(Long id) throws NotFoundException;

	public ResponseEntity<byte[]> generateGroupeReport(Long groupeId) throws IOException, JRException;

	public VisitanteDto saveWithDocs(VisitanteSaveRequest visitante) throws IOException, Exception;

	public Visitante save(Visitante visitante);

	public VisitanteDto updateWithDocs(VisitanteWithGroupeSaveRequest param) throws Exception;
	
	public VisitanteDto updateWithGroupeAndDocs(VisitanteWithGroupeSaveRequest param) throws Exception;

	public Visitante addToGroup(Long visitanteId, Long GroupId) throws NoSuchElementException;

	public VisitanteDto saveByAdmin(VisitanteWithGroupeSaveRequest visitante) throws IOException, Exception;

	public VisitanteDto saveWithGroupeByAdmin(VisitanteWithGroupeSaveRequest visitante) throws IOException, Exception;

	public List<Visitante> findByGroup(Long groupId);

	public void deleteById(Long id) throws NotFoundException;

	public ImageSide getImageSide(Long id);

}
