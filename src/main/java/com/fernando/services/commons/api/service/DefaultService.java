package com.fernando.services.commons.api.service;

import java.util.List;

import com.fernando.services.commons.api.model.DefaultEntity;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface DefaultService<T extends DefaultEntity<K>, K> {

    public void initRepository();
    
    public T get(K id);

    public List<T> listAll();
    public List<T> listAll(Sort sort);
    public Page<T> listAll(Pageable pageable);
    public List<T> listByExample(Example<T> example);
    public List<T> listByExample(Example<T> example, Sort sort);
    public Page<T> listByExample(Example<T> example, Pageable pageable);

    public T create(T entity);

    public T update(T entity);

    public void delete(T entity);
    
}
