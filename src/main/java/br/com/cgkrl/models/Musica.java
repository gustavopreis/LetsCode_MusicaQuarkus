package br.com.cgkrl.models;

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
@Table(name="musica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uid;
    @ManyToOne(fetch = FetchType.EAGER)
    private GeneroMusical generoMusical;
    private String nome;
    private String autor;
    private Long duracao; //milliseconds
    @ManyToMany(mappedBy = "musicas")
    private List<Playlist> playlists;
}
