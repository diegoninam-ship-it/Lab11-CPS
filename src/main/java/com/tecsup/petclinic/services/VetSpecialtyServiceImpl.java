package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetSpecialtyMapper     vetSpecialtyMapper;

    public VetSpecialtyServiceImpl(VetSpecialtyRepository vetSpecialtyRepository,
                                   VetSpecialtyMapper vetSpecialtyMapper) {
        this.vetSpecialtyRepository = vetSpecialtyRepository;
        this.vetSpecialtyMapper     = vetSpecialtyMapper;
    }

    @Override
    public VetSpecialtyDTO assignSpecialty(VetSpecialtyDTO vetSpecialtyDTO) {
        VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(vetSpecialtyDTO));
        return vetSpecialtyMapper.mapToDto(saved);
    }

    @Override
    public List<VetSpecialtyDTO> findSpecialtiesByVetId(Integer vetId) {
        List<VetSpecialty> list = vetSpecialtyRepository.findByVetId(vetId);
        list.forEach(vs -> log.info("" + vs));
        return list.stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetSpecialtyDTO> findVetsBySpecialtyId(Integer specialtyId) {
        List<VetSpecialty> list = vetSpecialtyRepository.findBySpecialtyId(specialtyId);
        list.forEach(vs -> log.info("" + vs));
        return list.stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeVetSpecialty(Integer id) throws VetSpecialtyNotFoundException {
        VetSpecialtyDTO dto = findById(id);
        vetSpecialtyRepository.delete(vetSpecialtyMapper.mapToEntity(dto));
    }

    @Override
    public VetSpecialtyDTO findById(Integer id) throws VetSpecialtyNotFoundException {
        Optional<VetSpecialty> vs = vetSpecialtyRepository.findById(id);
        if (!vs.isPresent())
            throw new VetSpecialtyNotFoundException("VetSpecialty not found with id: " + id);
        return vetSpecialtyMapper.mapToDto(vs.get());
    }
}
