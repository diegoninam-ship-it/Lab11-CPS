package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyMapper {

    public SpecialtyDTO mapToDto(Specialty specialty) {
        return SpecialtyDTO.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .build();
    }

    public Specialty mapToEntity(SpecialtyDTO dto) {
        return new Specialty(
                dto.getId(),
                dto.getName()
        );
    }
}