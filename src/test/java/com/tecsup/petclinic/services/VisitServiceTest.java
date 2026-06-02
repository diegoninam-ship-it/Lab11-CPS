package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.mappers.VisitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.repositories.VisitRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitMapper visitMapper;

    @MockitoBean
    private VisitRepository repository;

    @BeforeEach
    void setUp() {
    }

    /**
     * BUSCAR POR ID
     */
    @Test
    public void testFindVisitById() {

        Visit visitExpected = new Visit(1, LocalDate.of(2023, 1, 1), "Control anual", 7);

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(visitExpected));

        VisitDTO visit = null;

        try {
            visit = this.visitService.findById(1);
        } catch (VisitNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("" + visitExpected);
        log.info("" + visit);
        assertEquals(visitExpected.getDescription(), visit.getDescription());
        assertEquals(visitExpected.getPetId(), visit.getPetId());
    }

    /**
     * BUSCAR POR PET ID
     */
    @Test
    public void testFindVisitByPetId() {

        int PET_ID = 7;

        List<Visit> visitsExpected = List.of(
                new Visit(1, LocalDate.of(2023, 1, 1), "Control anual",    PET_ID),
                new Visit(2, LocalDate.of(2023, 6, 15), "Vacunación",       PET_ID)
        );

        Mockito.when(this.repository.findByPetId(PET_ID))
                .thenReturn(visitsExpected);

        List<VisitDTO> visits = this.visitService.findByPetId(PET_ID);

        assertEquals(visitsExpected.size(), visits.size());
    }

    /**
     * CREAR
     */
    @Test
    public void testCreateVisit() {

        Visit newVisit        = new Visit(null, LocalDate.now(), "Revisión general", 1);
        Visit newVisitCreated = new Visit(20,   LocalDate.now(), "Revisión general", 1);

        VisitDTO newVisitDTO         = this.visitMapper.mapToDto(newVisit);
        VisitDTO hopeVisitDTOCreated = this.visitMapper.mapToDto(newVisitCreated);

        Mockito.when(this.repository.save(newVisit))
                .thenReturn(newVisitCreated);

        VisitDTO newVisitDTOCreated = this.visitService.create(newVisitDTO);

        log.info("Visit created: {}", newVisitDTOCreated);

        assertNotNull(newVisitDTOCreated.getId());
        assertEquals(hopeVisitDTOCreated.getDescription(), newVisitDTOCreated.getDescription());
        assertEquals(hopeVisitDTOCreated.getPetId(),       newVisitDTOCreated.getPetId());
    }

    /**
     * ACTUALIZAR
     */
    @Test
    public void testUpdateVisit() {

        String UP_DESCRIPTION = "Vacunación de seguimiento";

        Visit newVisit        = new Visit(null, LocalDate.now(), "Vacunación inicial", 1);
        Visit newVisitCreated = new Visit(21,   LocalDate.now(), "Vacunación inicial", 1);

        VisitDTO newVisitDTO = visitMapper.mapToDto(newVisit);

        // Crear
        Mockito.when(this.repository.save(newVisit))
                .thenReturn(newVisitCreated);

        VisitDTO newVisitDTOCreated = this.visitService.create(newVisitDTO);
        log.info("{}", newVisitDTOCreated);

        // Actualizar
        newVisitDTOCreated.setDescription(UP_DESCRIPTION);
        Visit visitToUpdate = this.visitMapper.mapToEntity(newVisitDTOCreated);

        Mockito.when(this.repository.save(visitToUpdate))
                .thenReturn(visitToUpdate);

        VisitDTO visitDTOUpdated = this.visitService.update(newVisitDTOCreated);
        log.info("{}", visitDTOUpdated);

        assertEquals(UP_DESCRIPTION, visitDTOUpdated.getDescription());
        assertEquals(newVisitDTOCreated.getId(), visitDTOUpdated.getId());
    }

    /**
     * ELIMINAR
     */
    @Test
    public void testDeleteVisit() {

        Visit newVisit        = new Visit(null, LocalDate.now(), "Visita a eliminar", 1);
        Visit newVisitCreated = new Visit(22,   LocalDate.now(), "Visita a eliminar", 1);

        VisitDTO newVisitDTO = this.visitMapper.mapToDto(newVisit);

        // Crear
        Mockito.when(this.repository.save(newVisit))
                .thenReturn(newVisitCreated);

        VisitDTO visitDTOCreated = this.visitService.create(newVisitDTO);
        log.info("{}", visitDTOCreated);

        // Eliminar
        Mockito.doNothing().when(this.repository).delete(newVisitCreated);
        Mockito.when(this.repository.findById(newVisitCreated.getId()))
                .thenReturn(Optional.of(newVisitCreated));

        try {
            this.visitService.delete(visitDTOCreated.getId());
        } catch (VisitNotFoundException e) {
            fail(e.getMessage());
        }

        // Validar que ya no existe
        Mockito.when(this.repository.findById(newVisitCreated.getId()))
                .thenReturn(Optional.ofNullable(null));

        try {
            this.visitService.findById(visitDTOCreated.getId());
            assertTrue(false);
        } catch (VisitNotFoundException e) {
            assertTrue(true);
        }
    }
}