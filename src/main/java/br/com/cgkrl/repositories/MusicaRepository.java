package br.com.cgkrl.repositories;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.cgkrl.models.Musica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MusicaRepository implements PanacheRepository<Musica> {
    
    public Musica findByUid(String uid) {
        return find("uid", uid).firstResult();
    }

    public Musica findByNome(String nome) {
        return find("nome", nome).firstResult();
    }

    public List<Musica> findByPlaylistUid(String playlistUid) {
        return find("SELECT m FROM Musica m LEFT JOIN FETCH m.playlists p WHERE p.uid = ?1", playlistUid).list();
    }    
}
