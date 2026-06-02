package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class PetTypeServiceTest {

    @Autowired
    private TypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @MockitoBean
    private TypeRepository typeRepository;

    /**
     * BUSCAR POR ID
     */
    @Test
    public void testFindTypeById() {

        Type typeExpected = new Type(1, "cat");

        Mockito.when(this.typeRepository.findById(1))
                .thenReturn(Optional.of(typeExpected));

        TypeDTO type = null;
        try {
            type = this.typeService.findById(1);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("Expected: {}", typeExpected);
        log.info("Found: {}", type);
        assertEquals(typeExpected.getName(), type.getName());
    }

    /**
     * CREAR
     */
    @Test
    public void testCreateType() {

        Type newType        = new Type(null, "reptile");
        Type newTypeCreated = new Type(10,   "reptile");

        TypeDTO newTypeDTO            = typeMapper.mapToDto(newType);
        TypeDTO hopeTypeDTOCreated    = typeMapper.mapToDto(newTypeCreated);

        Mockito.when(this.typeRepository.save(newType))
                .thenReturn(newTypeCreated);

        TypeDTO newTypeDTOCreated = this.typeService.create(newTypeDTO);

        log.info("Type created: {}", newTypeDTOCreated);

        assertNotNull(newTypeDTOCreated.getId());
        assertEquals(hopeTypeDTOCreated.getName(), newTypeDTOCreated.getName());
    }

    /**
     * ACTUALIZAR
     */
    @Test
    public void testUpdateType() {

        String UP_NAME = "bird";

        Type newType        = new Type(null, "lizard");
        Type newTypeCreated = new Type(11,   "lizard");

        TypeDTO newTypeDTO = typeMapper.mapToDto(newType);

        Mockito.when(this.typeRepository.save(newType))
                .thenReturn(newTypeCreated);

        TypeDTO newTypeDTOCreated = this.typeService.create(newTypeDTO);
        log.info("Created: {}", newTypeDTOCreated);

        newTypeDTOCreated.setName(UP_NAME);
        Type typeToUpdate = typeMapper.mapToEntity(newTypeDTOCreated);

        Mockito.when(this.typeRepository.save(typeToUpdate))
                .thenReturn(typeToUpdate);

        TypeDTO typeDTOUpdated = this.typeService.update(newTypeDTOCreated);
        log.info("Updated: {}", typeDTOUpdated);

        assertEquals(UP_NAME, typeDTOUpdated.getName());
        assertEquals(newTypeDTOCreated.getId(), typeDTOUpdated.getId());
    }

    /**
     * ELIMINAR
     */
    @Test
    public void testDeleteType() {

        Type newType        = new Type(null, "hamster");
        Type newTypeCreated = new Type(12,   "hamster");

        TypeDTO newTypeDTO = typeMapper.mapToDto(newType);

        Mockito.when(this.typeRepository.save(newType))
                .thenReturn(newTypeCreated);

        TypeDTO typeDTOCreated = this.typeService.create(newTypeDTO);
        log.info("Created: {}", typeDTOCreated);

        Mockito.doNothing().when(this.typeRepository).delete(newTypeCreated);
        Mockito.when(this.typeRepository.findById(newTypeCreated.getId()))
                .thenReturn(Optional.of(newTypeCreated));

        try {
            this.typeService.delete(typeDTOCreated.getId());
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        Mockito.when(this.typeRepository.findById(newTypeCreated.getId()))
                .thenReturn(Optional.empty());

        try {
            this.typeService.findById(typeDTOCreated.getId());
            assertTrue(false);
        } catch (TypeNotFoundException e) {
            assertTrue(true);
        }
    }
}
