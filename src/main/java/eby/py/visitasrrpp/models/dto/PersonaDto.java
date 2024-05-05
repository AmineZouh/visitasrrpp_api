package eby.py.visitasrrpp.models.dto;

import eby.py.visitasrrpp.models.entity.Pais;

import eby.py.visitasrrpp.models.entity.TipoDocumento;

public class PersonaDto {

	private Long personaId;
	private String nombre;
	private String apellido;
	private String telefono;
	private String email;
	private TipoDocumento tipoDocumento;
	private Pais pais;
	private Character sexo;
	private String codigoTelefono;

	public Long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Long personaId) {
		this.personaId = personaId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public String getCodigoTelefono() {
		return codigoTelefono;
	}

	public void setCodigoTelefono(String codigoTelefono) {
		this.codigoTelefono = codigoTelefono;
	}	

}
