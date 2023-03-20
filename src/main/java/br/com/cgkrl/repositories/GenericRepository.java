package br.com.cgkrl.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public abstract class GenericRepository<T> implements PanacheRepository<T>{
    public T findByUid(String uid) {
        return find("uid", uid).firstResult();
    }
}
