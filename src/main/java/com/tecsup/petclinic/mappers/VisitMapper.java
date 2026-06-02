package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {

    public VisitDTO mapToDto(Visit visit) {
        return VisitDTO.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .description(visit.getDescription())
                .petId(visit.getPetId())
                .build();
    }

    public Visit mapToEntity(VisitDTO dto) {
        return new Visit(
                dto.getId(),
                dto.getVisitDate(),
                dto.getDescription(),
                dto.getPetId()
        );
    }
}