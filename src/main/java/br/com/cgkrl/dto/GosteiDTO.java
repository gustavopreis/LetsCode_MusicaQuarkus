package br.com.cgkrl.dto;

import javax.validation.constraints.NotEmpty;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GosteiDTO {
    @Schema(hidden = true)
    private String uid; 
    @NotEmpty(message = "Usuário obrigatório")
    private UsuarioDTO usuario;
    @NotEmpty(message = "Música obrigatório")
    private MusicaDTO musica;
}
