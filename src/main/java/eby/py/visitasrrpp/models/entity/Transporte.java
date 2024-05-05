package eby.py.visitasrrpp.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transportes")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transporte {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTransporte;

	@Column(name = "nro_chapa")
	private String nroChapa; //plate num

	@ManyToOne
	@JoinColumn(name = "tipo_transporte")
	private TipoTransporte tipoTransporte;



}
