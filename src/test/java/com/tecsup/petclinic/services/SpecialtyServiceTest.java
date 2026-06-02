package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class SpecialtyServiceTest {

	@Autowired
	private SpecialtyService specialtyService;

	@Autowired
	private SpecialtyMapper specialtyMapper;

	@MockitoBean
	private SpecialtyRepository repository;

	@BeforeEach
	void setUp() {
	}

	/**
	 * BUSCAR POR ID
	 */
	@Test
	public void testFindSpecialtyById() {

		Specialty specialtyExpected = new Specialty(1, "radiology");

		Mockito.when(this.repository.findById(1))
				.thenReturn(Optional.of(specialtyExpected));

		SpecialtyDTO specialty = null;

		try {
			specialty = this.specialtyService.findById(1);
		} catch (SpecialtyNotFoundException e) {
			fail(e.getMessage());
		}

		log.info("" + specialtyExpected);
		log.info("" + specialty);
		assertEquals(specialtyExpected.getName(), specialty.getName());
	}

	/**
	 * BUSCAR POR NOMBRE
	 */
	@Test
	public void testFindSpecialtyByName() {

		String FIND_NAME = "radiology";

		List<Specialty> specialtiesExpected = List.of(
				new Specialty(1, "radiology")
		);

		Mockito.when(this.repository.findByName(FIND_NAME))
				.thenReturn(specialtiesExpected);

		List<SpecialtyDTO> specialties = this.specialtyService.findByName(FIND_NAME);

		assertEquals(specialtiesExpected.size(), specialties.size());
	}

	/**
	 * CREAR
	 */
	@Test
	public void testCreateSpecialty() {

		Specialty newSpecialty        = new Specialty(null, "Cardiología");
		Specialty newSpecialtyCreated = new Specialty(10,   "Cardiología");

		SpecialtyDTO newSpecialtyDTO        = this.specialtyMapper.mapToDto(newSpecialty);
		SpecialtyDTO hopeSpecialtyDTOCreated = this.specialtyMapper.mapToDto(newSpecialtyCreated);

		Mockito.when(this.repository.save(newSpecialty))
				.thenReturn(newSpecialtyCreated);

		SpecialtyDTO newSpecialtyDTOCreated = this.specialtyService.create(newSpecialtyDTO);

		log.info("Specialty created: {}", newSpecialtyDTOCreated);

		assertNotNull(newSpecialtyDTOCreated.getId());
		assertEquals(hopeSpecialtyDTOCreated.getName(), newSpecialtyDTOCreated.getName());
	}

	/**
	 * ACTUALIZAR
	 */
	@Test
	public void testUpdateSpecialty() {

		String UP_NAME = "Dermatología Avanzada";

		Specialty newSpecialty        = new Specialty(null, "Dermatología");
		Specialty newSpecialtyCreated = new Specialty(11,   "Dermatología");

		SpecialtyDTO newSpecialtyDTO = specialtyMapper.mapToDto(newSpecialty);

		// Crear
		Mockito.when(this.repository.save(newSpecialty))
				.thenReturn(newSpecialtyCreated);

		SpecialtyDTO newSpecialtyDTOCreated = this.specialtyService.create(newSpecialtyDTO);
		log.info("{}", newSpecialtyDTOCreated);

		// Actualizar
		newSpecialtyDTOCreated.setName(UP_NAME);
		Specialty specialtyToUpdate = this.specialtyMapper.mapToEntity(newSpecialtyDTOCreated);

		Mockito.when(this.repository.save(specialtyToUpdate))
				.thenReturn(specialtyToUpdate);

		SpecialtyDTO specialtyDTOUpdated = this.specialtyService.update(newSpecialtyDTOCreated);
		log.info("{}", specialtyDTOUpdated);

		assertEquals(UP_NAME, specialtyDTOUpdated.getName());
		assertEquals(newSpecialtyDTOCreated.getId(), specialtyDTOUpdated.getId());
	}

	/**
	 * ELIMINAR
	 */
	@Test
	public void testDeleteSpecialty() {

		Specialty newSpecialty        = new Specialty(null, "Oftalmología");
		Specialty newSpecialtyCreated = new Specialty(12,   "Oftalmología");

		SpecialtyDTO newSpecialtyDTO = this.specialtyMapper.mapToDto(newSpecialty);

		// Crear
		Mockito.when(this.repository.save(newSpecialty))
				.thenReturn(newSpecialtyCreated);

		SpecialtyDTO specialtyDTOCreated = this.specialtyService.create(newSpecialtyDTO);
		log.info("{}", specialtyDTOCreated);

		// Eliminar
		Mockito.doNothing().when(this.repository).delete(newSpecialtyCreated);
		Mockito.when(this.repository.findById(newSpecialtyCreated.getId()))
				.thenReturn(Optional.of(newSpecialtyCreated));

		try {
			this.specialtyService.delete(specialtyDTOCreated.getId());
		} catch (SpecialtyNotFoundException e) {
			fail(e.getMessage());
		}

		// Validar que ya no existe
		Mockito.when(this.repository.findById(newSpecialtyCreated.getId()))
				.thenReturn(Optional.ofNullable(null));

		try {
			this.specialtyService.findById(specialtyDTOCreated.getId());
			assertTrue(false);
		} catch (SpecialtyNotFoundException e) {
			assertTrue(true);
		}
	}
}