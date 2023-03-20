package br.com.cgkrl.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="genero_musical")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneroMusical {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uid;
    private String nome;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "generoMusical")
    private Set<Musica> musicas;
}