package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialtyDTO assignSpecialty(VetSpecialtyDTO vetSpecialtyDTO);

    List<VetSpecialtyDTO> findSpecialtiesByVetId(Integer vetId);

    List<VetSpecialtyDTO> findVetsBySpecialtyId(Integer specialtyId);

    void removeVetSpecialty(Integer id) throws VetSpecialtyNotFoundException;

    VetSpecialtyDTO findById(Integer id) throws VetSpecialtyNotFoundException;
}
