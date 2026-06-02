package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

import java.util.List;

public interface TypeService {

    TypeDTO create(TypeDTO typeDTO);

    TypeDTO update(TypeDTO typeDTO);

    void delete(Integer id) throws TypeNotFoundException;

    TypeDTO findById(Integer id) throws TypeNotFoundException;

    List<TypeDTO> findByName(String name);

    List<Type> findAll();
}
