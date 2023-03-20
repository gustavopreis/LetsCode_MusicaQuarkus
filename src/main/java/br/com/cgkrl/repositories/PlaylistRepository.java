package br.com.cgkrl.repositories;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.cgkrl.models.Playlist;
import br.com.cgkrl.models.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PlaylistRepository implements PanacheRepository<Playlist> {
    
    public Playlist findByUid(String uid) {
        return find("uid", uid).firstResult();
    }

    public List<Playlist> findByUsuario(String usuarioUid) {
        return find("usuario.uid", usuarioUid).list();
    }    

    public Playlist findByNomeAndUsuario(String nome, Usuario usuario) {
        return find("nome = ?1 and usuario = ?2", nome, usuario).firstResult();
    }        
}
