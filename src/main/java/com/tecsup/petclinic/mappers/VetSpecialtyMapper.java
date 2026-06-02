package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import org.springframework.stereotype.Component;

@Component
public class VetSpecialtyMapper {

    public VetSpecialtyDTO mapToDto(VetSpecialty vs) {
        return VetSpecialtyDTO.builder()
                .id(vs.getId())
                .vetId(vs.getVetId())
                .specialtyId(vs.getSpecialtyId())
                .build();
    }

    public VetSpecialty mapToEntity(VetSpecialtyDTO dto) {
        return new VetSpecialty(
                dto.getId(),
                dto.getVetId(),
                dto.getSpecialtyId()
        );
    }
}
