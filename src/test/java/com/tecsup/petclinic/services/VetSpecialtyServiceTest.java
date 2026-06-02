package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
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
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetSpecialtyMapper vetSpecialtyMapper;

    @MockitoBean
    private VetSpecialtyRepository vetSpecialtyRepository;

    /**
     * ASIGNAR ESPECIALIDAD A VETERINARIO
     */
    @Test
    public void testAssignSpecialtyToVet() {

        VetSpecialty newVS        = new VetSpecialty(null, 1, 2);
        VetSpecialty newVSCreated = new VetSpecialty(10,   1, 2);

        VetSpecialtyDTO newVSDTO            = vetSpecialtyMapper.mapToDto(newVS);
        VetSpecialtyDTO hopeVSDTOCreated    = vetSpecialtyMapper.mapToDto(newVSCreated);

        Mockito.when(this.vetSpecialtyRepository.save(newVS))
                .thenReturn(newVSCreated);

        VetSpecialtyDTO vsDTOCreated = this.vetSpecialtyService.assignSpecialty(newVSDTO);

        log.info("VetSpecialty assigned: {}", vsDTOCreated);

        assertNotNull(vsDTOCreated.getId());
        assertEquals(hopeVSDTOCreated.getVetId(), vsDTOCreated.getVetId());
        assertEquals(hopeVSDTOCreated.getSpecialtyId(), vsDTOCreated.getSpecialtyId());
    }

    /**
     * BUSCAR ESPECIALIDADES POR VETERINARIO
     */
    @Test
    public void testFindSpecialtiesByVetId() {

        Integer VET_ID = 1;

        List<VetSpecialty> listExpected = List.of(
                new VetSpecialty(1, VET_ID, 1),
                new VetSpecialty(2, VET_ID, 3)
        );

        Mockito.when(this.vetSpecialtyRepository.findByVetId(VET_ID))
                .thenReturn(listExpected);

        List<VetSpecialtyDTO> result = this.vetSpecialtyService.findSpecialtiesByVetId(VET_ID);

        log.info("Specialties for vet {}: {}", VET_ID, result);

        assertEquals(listExpected.size(), result.size());
        result.forEach(dto -> assertEquals(VET_ID, dto.getVetId()));
    }

    /**
     * BUSCAR VETERINARIOS POR ESPECIALIDAD
     */
    @Test
    public void testFindVetsBySpecialtyId() {

        Integer SPECIALTY_ID = 2;

        List<VetSpecialty> listExpected = List.of(
                new VetSpecialty(3, 1, SPECIALTY_ID),
                new VetSpecialty(4, 2, SPECIALTY_ID),
                new VetSpecialty(5, 3, SPECIALTY_ID)
        );

        Mockito.when(this.vetSpecialtyRepository.findBySpecialtyId(SPECIALTY_ID))
                .thenReturn(listExpected);

        List<VetSpecialtyDTO> result = this.vetSpecialtyService.findVetsBySpecialtyId(SPECIALTY_ID);

        log.info("Vets for specialty {}: {}", SPECIALTY_ID, result);

        assertEquals(listExpected.size(), result.size());
        result.forEach(dto -> assertEquals(SPECIALTY_ID, dto.getSpecialtyId()));
    }

    /**
     * ELIMINAR RELACION VETERINARIO-ESPECIALIDAD
     */
    @Test
    public void testRemoveVetSpecialty() {

        VetSpecialty newVS        = new VetSpecialty(null, 2, 3);
        VetSpecialty newVSCreated = new VetSpecialty(20,   2, 3);

        VetSpecialtyDTO newVSDTO = vetSpecialtyMapper.mapToDto(newVS);

        Mockito.when(this.vetSpecialtyRepository.save(newVS))
                .thenReturn(newVSCreated);

        VetSpecialtyDTO vsDTOCreated = this.vetSpecialtyService.assignSpecialty(newVSDTO);
        log.info("Assigned: {}", vsDTOCreated);

        Mockito.doNothing().when(this.vetSpecialtyRepository).delete(newVSCreated);
        Mockito.when(this.vetSpecialtyRepository.findById(newVSCreated.getId()))
                .thenReturn(Optional.of(newVSCreated));

        try {
            this.vetSpecialtyService.removeVetSpecialty(vsDTOCreated.getId());
        } catch (VetSpecialtyNotFoundException e) {
            fail(e.getMessage());
        }

        Mockito.when(this.vetSpecialtyRepository.findById(newVSCreated.getId()))
                .thenReturn(Optional.empty());

        try {
            this.vetSpecialtyService.findById(vsDTOCreated.getId());
            assertTrue(false);
        } catch (VetSpecialtyNotFoundException e) {
            assertTrue(true);
        }
    }
}
