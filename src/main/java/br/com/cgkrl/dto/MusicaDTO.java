package br.com.cgkrl.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MusicaDTO {
    private String uid;
    @NotEmpty(message = "Nome obrigatório")
    private String nome;
    private String autor;
    @NotEmpty(message = "Duração obrigatória")
    private Long duracao;
    @NotEmpty(message = "Gênero Musical obrigatório")
    private GeneroMusicalDTO generoMusical;

}
