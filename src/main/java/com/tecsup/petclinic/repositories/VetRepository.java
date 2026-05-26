package com.tecsup.petclinic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecsup.petclinic.entities.Vet;

@Repository
public interface VetRepository extends JpaRepository<Vet, Integer>{

    List<Vet> findByLastName(String lastName);

    List<Vet> findByEmail(String email);

    List<Vet> findByActive(Boolean active);

    @Override
    List<Vet> findAll();
}
