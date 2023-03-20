package br.com.cgkrl.repositories;

import javax.enterprise.context.ApplicationScoped;

import br.com.cgkrl.models.GeneroMusical;

@ApplicationScoped
public class GeneroMusicalRepository extends GenericRepository<GeneroMusical> {

    public GeneroMusical findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
    
}
