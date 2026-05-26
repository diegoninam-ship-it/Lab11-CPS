package com.tecsup.petclinic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.mappers.VetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;

@Service
@Slf4j
public class VetServiceImpl implements VetService {

    VetRepository vetRepository;
    VetMapper vetMapper;

    public VetServiceImpl(VetRepository vetRepository, VetMapper vetMapper) {
        this.vetRepository = vetRepository;
        this.vetMapper = vetMapper;
    }

    @Override
    public VetDTO create(VetDTO vetDTO) {
        Vet newVet = vetRepository.save(vetMapper.mapToEntity(vetDTO));
        return vetMapper.mapToDto(newVet);
    }

    @Override
    public VetDTO update(VetDTO vetDTO) {
        Vet newVet = vetRepository.save(vetMapper.mapToEntity(vetDTO));
        return vetMapper.mapToDto(newVet);
    }

    @Override
    public void delete(Integer id) throws VetNotFoundException {
        VetDTO vet = findById(id);
        vetRepository.delete(this.vetMapper.mapToEntity(vet));
    }

    @Override
    public VetDTO findById(Integer id) throws VetNotFoundException {
        Optional<Vet> vet = vetRepository.findById(id);
        if (!vet.isPresent())
            throw new VetNotFoundException("Record not found...!");
        return this.vetMapper.mapToDto(vet.get());
    }

    @Override
    public List<VetDTO> findByLastName(String lastName) {
        List<Vet> vets = vetRepository.findByLastName(lastName);
        vets.forEach(vet -> log.info("" + vet));
        return vets.stream()
                .map(this.vetMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vet> findByEmail(String email) {
        List<Vet> vets = vetRepository.findByEmail(email);
        vets.forEach(vet -> log.info("" + vet));
        return vets;
    }

    @Override
    public List<Vet> findByActive(Boolean active) {
        List<Vet> vets = vetRepository.findByActive(active);
        vets.forEach(vet -> log.info("" + vet));
        return vets;
    }

    @Override
    public List<Vet> findAll() {
        return vetRepository.findAll();
    }
}
