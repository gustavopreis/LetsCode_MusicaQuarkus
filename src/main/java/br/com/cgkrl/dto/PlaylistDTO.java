package br.com.cgkrl.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistDTO {
    private String uid;
    @NotEmpty(message = "Nome obrigatório")
    private String nome;
    @NotEmpty(message = "Usuário obrigatório")
    private UsuarioDTO usuario;
    private List<MusicaDTO> musicas;
}
