package com.fernando.services.commons.api.service;

import java.util.List;

import com.fernando.services.commons.api.exception.EntityNotFoundException;
import com.fernando.services.commons.api.model.DefaultEntity;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;


public abstract class DefaultServiceImpl<T extends DefaultEntity<K>, K> implements DefaultService<T, K> {

    protected MongoRepository<T, K> defaultRepository;


    protected void setRepository(MongoRepository<T, K> repository) {
        defaultRepository = repository;
    }

    @Override
    public T get(K id) {
        return defaultRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<T> listAll() {
        return defaultRepository.findAll();
    }

    @Override
    public List<T> listAll(Sort sort) {
        return defaultRepository.findAll(sort);
    }

    @Override
    public Page<T> listAll(Pageable pageable) {
        return defaultRepository.findAll(pageable);
    }

    @Override
    public List<T> listByExample(Example<T> example) {
        return defaultRepository.findAll(example);
    }

    @Override
    public List<T> listByExample(Example<T> example, Sort sort) {
        return defaultRepository.findAll(example, sort);
    }

    @Override
    public Page<T> listByExample(Example<T> example, Pageable pageable) {
        return defaultRepository.findAll(example, pageable);
    }

    @Override
    public T create(T entity) {
        return defaultRepository.insert(entity);
    }

    @Override
    public T update(T entity) {
        return defaultRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        defaultRepository.delete(entity);
    }

}
