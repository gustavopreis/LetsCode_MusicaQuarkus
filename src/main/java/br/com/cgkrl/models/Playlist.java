package br.com.cgkrl.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="playlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uid;
    private String nome;
    @ManyToOne
    private Usuario usuario;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Musica> musicas;

    public List<Musica> getMusicas(){
        return (musicas!=null? this.musicas : new ArrayList<>());
    }
}