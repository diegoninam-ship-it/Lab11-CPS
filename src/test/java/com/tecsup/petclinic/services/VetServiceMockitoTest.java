package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.mappers.VetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class VetServiceMockitoTest {

    @Autowired
    private VetService vetService;

    @Autowired
    private VetMapper vetMapper;

    @MockitoBean
    private VetRepository repository;

    @BeforeEach
    void setUp() {}

    // ── BUSQUEDA POR ID ───────────────────────────────────────────────────────

    @Test
    public void testFindVetById() {

        Vet vetExpected = new Vet(1, "James", "Carter",
                "james.carter@petclinic.com", "6085551234", true);

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(vetExpected));

        VetDTO vet = null;
        try {
            vet = this.vetService.findById(1);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("" + vetExpected);
        log.info("" + vet);
        assertEquals(vetExpected.getLastName(), vet.getLastName());
    }

    // ── BUSQUEDA POR APELLIDO ─────────────────────────────────────────────────

    @Test
    public void testFindVetByLastName() {

        String FIND_LAST_NAME = "Davis";

        List<Vet> vetsExpected = List.of(
                new Vet(2, "Betty",  "Davis", "betty.davis@petclinic.com",  "6085551749", true),
                new Vet(4, "Harold", "Davis", "harold.davis@petclinic.com", "6085553198", true)
        );

        Mockito.when(this.repository.findByLastName(FIND_LAST_NAME))
                .thenReturn(vetsExpected);

        List<VetDTO> vets = this.vetService.findByLastName(FIND_LAST_NAME);

        assertEquals(vetsExpected.size(), vets.size());
    }

    // ── BUSQUEDA POR EMAIL ────────────────────────────────────────────────────

    @Test
    public void testFindVetByEmail() {

        String FIND_EMAIL = "james.carter@petclinic.com";

        List<Vet> vetsExpected = List.of(
                new Vet(1, "James", "Carter", FIND_EMAIL, "6085551234", true)
        );

        Mockito.when(this.repository.findByEmail(FIND_EMAIL))
                .thenReturn(vetsExpected);

        List<Vet> vets = this.vetService.findByEmail(FIND_EMAIL);

        assertEquals(vetsExpected.size(), vets.size());
    }

    // ── BUSQUEDA POR ESTADO ACTIVO ────────────────────────────────────────────

    @Test
    public void testFindVetByActive() {

        Boolean ACTIVE = true;

        List<Vet> vetsExpected = List.of(
                new Vet(1, "James", "Carter", "james.carter@petclinic.com", "6085551234", true),
                new Vet(2, "Helen", "Leary",  "helen.leary@petclinic.com",  "6085552345", true),
                new Vet(3, "Linda", "Douglas","linda.douglas@petclinic.com","6085553456", true)
        );

        Mockito.when(this.repository.findByActive(ACTIVE))
                .thenReturn(vetsExpected);

        List<Vet> vets = this.vetService.findByActive(ACTIVE);

        assertEquals(vetsExpected.size(), vets.size());
    }

    // ── CREACION ──────────────────────────────────────────────────────────────

    @Test
    public void testCreateVet() {

        Vet newVet = new Vet();
        newVet.setFirstName("Diego");
        newVet.setLastName("Ninam");
        newVet.setEmail("diego.ninam@petclinic.com");
        newVet.setPhone("999111222");
        newVet.setActive(true);

        Vet newVetCreated = new Vet(1000, "Diego", "Ninam",
                "diego.ninam@petclinic.com", "999111222", true);

        VetDTO newVetDTO = this.vetMapper.mapToDto(newVet);
        VetDTO hopeVetDTOCreated = this.vetMapper.mapToDto(newVetCreated);

        Mockito.when(this.repository.save(newVet))
                .thenReturn(newVetCreated);

        VetDTO newVetDTOCreated = this.vetService.create(newVetDTO);

        log.info("Vet created: {}", newVetDTOCreated);

        assertNotNull(newVetDTOCreated.getId());
        assertEquals(hopeVetDTOCreated.getFirstName(), newVetDTOCreated.getFirstName());
        assertEquals(hopeVetDTOCreated.getLastName(),  newVetDTOCreated.getLastName());
        assertEquals(hopeVetDTOCreated.getEmail(),     newVetDTOCreated.getEmail());
        assertEquals(hopeVetDTOCreated.getPhone(),     newVetDTOCreated.getPhone());
        assertEquals(hopeVetDTOCreated.getActive(),    newVetDTOCreated.getActive());
    }

    // ── ACTUALIZACION ─────────────────────────────────────────────────────────

    @Test
    public void testUpdateVet() {

        String UP_FIRST_NAME = "Marco2";
        String UP_LAST_NAME  = "PoloUpdated";
        String UP_EMAIL      = "marco.updated@petclinic.com";
        String UP_PHONE      = "988999000";
        Boolean UP_ACTIVE    = false;

        Vet newVet    = new Vet(0, "Marco", "Polo", "marco.polo@petclinic.com", "988111000", true);
        Vet newVetCreated = new Vet(4000, "Marco", "Polo", "marco.polo@petclinic.com", "988111000", true);

        VetDTO newVetDTO = vetMapper.mapToDto(newVet);

        // ------------ Create ---------------
        Mockito.when(this.repository.save(newVet)).thenReturn(newVetCreated);
        VetDTO newVetDTOCreate = this.vetService.create(newVetDTO);
        log.info("{}", newVetDTOCreate);

        // ------------ Update ---------------
        newVetDTOCreate.setFirstName(UP_FIRST_NAME);
        newVetDTOCreate.setLastName(UP_LAST_NAME);
        newVetDTOCreate.setEmail(UP_EMAIL);
        newVetDTOCreate.setPhone(UP_PHONE);
        newVetDTOCreate.setActive(UP_ACTIVE);

        Vet newVetUpdate = this.vetMapper.mapToEntity(newVetDTOCreate);

        Mockito.when(this.repository.save(newVetUpdate)).thenReturn(newVetUpdate);

        VetDTO vetDTOUpdate = this.vetService.update(newVetDTOCreate);
        log.info("{}", vetDTOUpdate);

        assertEquals(UP_FIRST_NAME, vetDTOUpdate.getFirstName());
        assertEquals(UP_LAST_NAME,  vetDTOUpdate.getLastName());
        assertEquals(UP_EMAIL,      vetDTOUpdate.getEmail());
        assertEquals(UP_PHONE,      vetDTOUpdate.getPhone());
        assertEquals(UP_ACTIVE,     vetDTOUpdate.getActive());
    }

    // ── ELIMINACION ───────────────────────────────────────────────────────────

    @Test
    public void testDeleteVet() {

        Vet newVet        = new Vet(0, "Lucia", "Quispe", "lucia.quispe@petclinic.com", "977222111", true);
        Vet newVetCreated = new Vet(2000, "Lucia", "Quispe", "lucia.quispe@petclinic.com", "977222111", true);

        VetDTO newVetDTO = this.vetMapper.mapToDto(newVet);

        // ------------ Create ---------------
        Mockito.when(this.repository.save(newVet)).thenReturn(newVetCreated);
        VetDTO vetDTOCreate = this.vetService.create(newVetDTO);
        log.info("{}", vetDTOCreate);

        // ------------ Delete ---------------
        Mockito.doNothing().when(this.repository).delete(newVetCreated);
        Mockito.when(this.repository.findById(newVetCreated.getId()))
                .thenReturn(Optional.of(newVetCreated));

        try {
            this.vetService.delete(vetDTOCreate.getId());
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        // ------------ Validate ---------------
        Mockito.when(this.repository.findById(newVetCreated.getId()))
                .thenReturn(Optional.ofNullable(null));

        try {
            this.vetService.findById(vetDTOCreate.getId());
            assertTrue(false);
        } catch (VetNotFoundException e) {
            assertTrue(true);
        }
    }

	
}
