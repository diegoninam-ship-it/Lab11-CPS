package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

public interface OwnerService {

    OwnerDTO create(OwnerDTO ownerDTO);

    OwnerDTO update(OwnerDTO ownerDTO);

    void delete(Integer id) throws OwnerNotFoundException;

    OwnerDTO findById(Integer id) throws OwnerNotFoundException;

    List<OwnerDTO> findByLastName(String lastName);

    List<Owner> findByCity(String city);

    List<Owner> findByTelephone(String telephone);

    List<Owner> findAll();
}
