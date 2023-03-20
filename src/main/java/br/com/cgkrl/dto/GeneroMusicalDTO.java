package br.com.cgkrl.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneroMusicalDTO {
    
    private String uid;
    @NotEmpty(message = "Nome obrigatório")
    private String nome;
    
}
