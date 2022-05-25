package com.epam.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;


public abstract class GenericServiceImpl<T, ID extends Serializable, R extends JpaRepository<T, ID>> {

    @Autowired
    protected R r;

    public List<T> findAll() {
        return r.findAll();
    }

    public T save(T entity) {
        return r.save(entity);
    }

    public T findById(ID id){
        return r.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public void deleteById(ID id) {
        r.deleteById(id);
    }
}
