package eby.py.visitasrrpp.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_transportes")
public class TipoTransporte {
	//Transport Type
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTipoTrasporte;
	private String nombre;

	public Long getIdTipoTrasporte() {
		return idTipoTrasporte;
	}

	public void setIdTipoTrasporte(Long idTipoTrasporte) {
		this.idTipoTrasporte = idTipoTrasporte;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
