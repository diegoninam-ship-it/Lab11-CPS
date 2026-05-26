package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import org.springframework.stereotype.Component;

@Component
public class VetMapper {
    public Vet mapToEntity(VetDTO dto) {
        if (dto == null) return null;
        return new Vet(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getActive()
        );
    }

    public VetDTO mapToDto(Vet entity) {
        if (entity == null) return null;
        return new VetDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getActive()
        );
    }
}
