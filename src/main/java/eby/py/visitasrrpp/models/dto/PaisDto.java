package eby.py.visitasrrpp.models.dto;

public class PaisDto {

	private Long paisId;
	private String nombre;
	private String codigo;
	private String iddTel;
	private String bandera;

	public Long getPaisId() {
		return paisId;
	}

	public void setPaisId(Long paisId) {
		this.paisId = paisId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getIddTel() {
		return iddTel;
	}

	public void setIddTel(String iddTel) {
		this.iddTel = iddTel;
	}

	public String getBandera() {
		return bandera;
	}

	public void setBandera(String bandera) {
		this.bandera = bandera;
	}

}
