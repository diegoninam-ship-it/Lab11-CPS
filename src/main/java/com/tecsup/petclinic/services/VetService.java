package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

public interface VetService {
    VetDTO create(VetDTO vetDTO);

    VetDTO update(VetDTO vetDTO);

    void delete(Integer id) throws VetNotFoundException;

    VetDTO findById(Integer id) throws VetNotFoundException;

    List<VetDTO> findByLastName(String lastName);

    List<Vet> findByEmail(String email);

    List<Vet> findByActive(Boolean active);

    List<Vet> findAll();
}
