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
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long personaId;
	private String nombre;// name
	private String apellido; //last name

	@Column(name = "documento_nro")
	private String nroDocumento; //num document

	@Column(name = "telefono_nro")
	private String telefono;
	private String email;
	private String password;

	@ManyToOne
	@JoinColumn(name = "tipo_documento")
	private TipoDocumento tipoDocumento; //Type document

	@ManyToOne
	@JoinColumn(name = "nacionalidad")
	private Pais pais;

	private Character sexo;
	private String codigoTelefono;



}
