package eby.py.visitasrrpp.models.dto;

import eby.py.visitasrrpp.models.entity.Pais;
import eby.py.visitasrrpp.models.entity.TipoDocumento;
import eby.py.visitasrrpp.models.entity.VisitanteGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitanteDto {

	private Long personaId;
	private String nombre;
	private String nroDocumento;
	private String apellido;
	private String telefono;
	private String email;
	private String idDocImageFrontSideUri;
	private String idDocImageBackSideUri;
	private TipoDocumento tipoDocumento;
	private Pais pais;
	private Character sexo;
	private String codigoTelefono;
	private VisitanteGroup group;

}
