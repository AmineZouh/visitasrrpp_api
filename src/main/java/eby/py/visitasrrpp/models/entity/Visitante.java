package eby.py.visitasrrpp.models.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="visitantes")
@PrimaryKeyJoinColumn(name = "visitante_id")
@Data
@ToString
public class Visitante extends Persona{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private VisitanteGroup group;
    @OneToOne
    @JoinColumn(name = "frontSide_id")
    private ImageSide frontSide;
    @OneToOne
    @JoinColumn(name = "backSide_id")
    private ImageSide backSide;
    private String idDocImageBackSideUri;
    private String idDocImageFrontSideUri;


    @Builder
    public Visitante(Long personaId, String nombre, String apellido, String nroDocumento, String telefono, String email, String password, TipoDocumento tipoDocumento, Pais pais, Character sexo, String codigoTelefono, VisitanteGroup group, ImageSide frontSide, ImageSide backSide, String idDocImageBackSideUri, String idDocImageFrontSideUri){
        super(personaId,nombre,apellido,nroDocumento,telefono,email, password, tipoDocumento,pais,sexo,codigoTelefono);
        this.group = group;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.idDocImageBackSideUri=idDocImageBackSideUri;
        this.idDocImageFrontSideUri=idDocImageFrontSideUri;
    }
    
}