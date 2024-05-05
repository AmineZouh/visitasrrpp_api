package eby.py.visitasrrpp.models.dto;

import org.springframework.web.multipart.MultipartFile;


import lombok.Data;

@Data
public class VisitanteSaveRequest {
	private String nombre;
	private String apellido;
	private String email;
	private String nroDocumento;
	private Long tipoDocumentoId;
	private Long paisId;
	private Character sexo;
    private MultipartFile idDocFront;
    private MultipartFile idDocBack;
	// private String telefono;
	// private String password;
	// private Long groupId;
	// private String codigoTelefono;
}
