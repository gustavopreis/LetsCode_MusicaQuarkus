package br.com.cgkrl.repositories;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.cgkrl.models.Gostei;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.models.Usuario;

@ApplicationScoped
public class GosteiRepository extends GenericRepository<Gostei> {

    public List<Gostei> findByUsuario(String usuarioUid) {
        return find("usuario.uid", usuarioUid).list();
    }    
    public List<Gostei> findByUsuarioAndMusica(Usuario usuario, Musica musica) {
        return find("usuario = ?1 and musica = ?2", usuario, musica).list();
    }    


}
