package br.com.cgkrl.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.com.cgkrl.models.Usuario;

@ApplicationScoped
public class UsuarioRepository extends GenericRepository<Usuario> {

    public Usuario findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
