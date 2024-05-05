package eby.py.visitasrrpp.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long personaId;
    private String nombre;
    private String apellido;
    private String email;
}
