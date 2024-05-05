package eby.py.visitasrrpp.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paises_api")
public class PaisApi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long idPais;
	public String nombre;
	public String codigo; //code
	@Column(name = "idd_tel")
	public String iddTel;
	private String bandera; //flag

	public long getIdPais() {
		return idPais;
	}

	public void setIdPais(long idPais) {
		this.idPais = idPais;
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