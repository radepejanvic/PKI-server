package com.example.pki.pkiapplication.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JpaService<T> {

    List<T> findAll();

    T findOne(Long id);

    T save(T object);

    void remove(T object);

}
