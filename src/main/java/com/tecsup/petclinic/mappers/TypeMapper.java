package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper {

    public TypeDTO mapToDto(Type type) {
        return TypeDTO.builder()
                .id(type.getId())
                .name(type.getName())
                .build();
    }

    public Type mapToEntity(TypeDTO dto) {
        return new Type(
                dto.getId(),
                dto.getName()
        );
    }
}
