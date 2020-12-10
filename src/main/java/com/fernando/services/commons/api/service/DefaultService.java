package com.fernando.services.commons.api.service;

import java.util.List;

import com.fernando.services.commons.api.model.DefaultEntity;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DefaultService<T extends DefaultEntity<K>, K> {
    
    public T get(K id);

    public List<T> list();

    public Page<T> list(Pageable pageable);

    public List<T> listByExample(Example<T> example);

    public T create(T entity);

    public T update(T entity);

    public void delete(T entity);
    
}
