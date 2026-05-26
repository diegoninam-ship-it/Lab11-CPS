package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.mappers.OwnerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class OwnerServiceMockitoTest {

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private OwnerMapper ownerMapper;

	@MockitoBean
	private OwnerRepository repository;

	@BeforeEach
	void setUp() {}

	// ── BUSQUEDA POR ID ───────────────────────────────────────────────────────

	@Test
	public void testFindOwnerById() {

		Owner ownerExpected = new Owner(1, "George", "Franklin",
				"110 W. Liberty St.", "Madison", "6085551023");

		Mockito.when(this.repository.findById(1))
				.thenReturn(Optional.of(ownerExpected));

		OwnerDTO owner = null;
		try {
			owner = this.ownerService.findById(1);
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}

		log.info("" + ownerExpected);
		log.info("" + owner);
		assertEquals(ownerExpected.getLastName(), owner.getLastName());
	}

	// ── BUSQUEDA POR APELLIDO ─────────────────────────────────────────────────

	@Test
	public void testFindOwnerByLastName() {

		String FIND_LAST_NAME = "Davis";

		List<Owner> ownersExpected = List.of(
				new Owner(2, "Betty",  "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749"),
				new Owner(4, "Harold", "Davis", "563 Friendly St.",  "Windsor",     "6085553198")
		);

		Mockito.when(this.repository.findByLastName(FIND_LAST_NAME))
				.thenReturn(ownersExpected);

		List<OwnerDTO> owners = this.ownerService.findByLastName(FIND_LAST_NAME);

		assertEquals(ownersExpected.size(), owners.size());
	}

	// ── BUSQUEDA POR CIUDAD ───────────────────────────────────────────────────

	@Test
	public void testFindOwnerByCity() {

		String FIND_CITY = "Madison";

		List<Owner> ownersExpected = List.of(
				new Owner(1, "George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023"),
				new Owner(5, "Peter",  "McTavish", "2387 S. Fair Way",   "Madison", "6085552765")
		);

		Mockito.when(this.repository.findByCity(FIND_CITY))
				.thenReturn(ownersExpected);

		List<Owner> owners = this.ownerService.findByCity(FIND_CITY);

		assertEquals(ownersExpected.size(), owners.size());
	}

	// ── BUSQUEDA POR TELEFONO ─────────────────────────────────────────────────

	@Test
	public void testFindOwnerByTelephone() {

		String FIND_TELEPHONE = "6085558763";

		List<Owner> ownersExpected = List.of(
				new Owner(3, "Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", FIND_TELEPHONE)
		);

		Mockito.when(this.repository.findByTelephone(FIND_TELEPHONE))
				.thenReturn(ownersExpected);

		List<Owner> owners = this.ownerService.findByTelephone(FIND_TELEPHONE);

		assertEquals(ownersExpected.size(), owners.size());
	}

	// ── CREACION ──────────────────────────────────────────────────────────────

	@Test
	public void testCreateOwner() {

		Owner newOwner = new Owner();
		newOwner.setFirstName("Diego");
		newOwner.setLastName("Ninam");
		newOwner.setAddress("Av. Siempre Viva 742");
		newOwner.setCity("Lima");
		newOwner.setTelephone("999888777");

		Owner newOwnerCreated = new Owner(1000, "Diego", "Ninam",
				"Av. Siempre Viva 742", "Lima", "999888777");

		OwnerDTO newOwnerDTO = this.ownerMapper.mapToDto(newOwner);
		OwnerDTO hopeOwnerDTOCreated = this.ownerMapper.mapToDto(newOwnerCreated);

		Mockito.when(this.repository.save(newOwner))
				.thenReturn(newOwnerCreated);

		OwnerDTO newOwnerDTOCreated = this.ownerService.create(newOwnerDTO);

		log.info("Owner created: {}", newOwnerDTOCreated);

		assertNotNull(newOwnerDTOCreated.getId());
		assertEquals(hopeOwnerDTOCreated.getFirstName(), newOwnerDTOCreated.getFirstName());
		assertEquals(hopeOwnerDTOCreated.getLastName(),  newOwnerDTOCreated.getLastName());
		assertEquals(hopeOwnerDTOCreated.getAddress(),   newOwnerDTOCreated.getAddress());
		assertEquals(hopeOwnerDTOCreated.getCity(),      newOwnerDTOCreated.getCity());
		assertEquals(hopeOwnerDTOCreated.getTelephone(), newOwnerDTOCreated.getTelephone());
	}

	// ── ACTUALIZACION ─────────────────────────────────────────────────────────

	@Test
	public void testUpdateOwner() {

		String UP_FIRST_NAME = "Carlos2";
		String UP_LAST_NAME  = "RamirezUpdated";
		String UP_ADDRESS    = "Calle Nueva 999";
		String UP_CITY       = "Cusco";
		String UP_TELEPHONE  = "933222111";

		Owner newOwner        = new Owner(0, "Carlos", "Ramirez", "Calle Las Flores 123", "Arequipa", "988777666");
		Owner newOwnerCreated = new Owner(4000, "Carlos", "Ramirez", "Calle Las Flores 123", "Arequipa", "988777666");

		OwnerDTO newOwnerDTO = ownerMapper.mapToDto(newOwner);

		// ------------ Create ---------------
		Mockito.when(this.repository.save(newOwner)).thenReturn(newOwnerCreated);
		OwnerDTO newOwnerDTOCreate = this.ownerService.create(newOwnerDTO);
		log.info("{}", newOwnerDTOCreate);

		// ------------ Update ---------------
		newOwnerDTOCreate.setFirstName(UP_FIRST_NAME);
		newOwnerDTOCreate.setLastName(UP_LAST_NAME);
		newOwnerDTOCreate.setAddress(UP_ADDRESS);
		newOwnerDTOCreate.setCity(UP_CITY);
		newOwnerDTOCreate.setTelephone(UP_TELEPHONE);

		Owner newOwnerUpdate = this.ownerMapper.mapToEntity(newOwnerDTOCreate);

		Mockito.when(this.repository.save(newOwnerUpdate)).thenReturn(newOwnerUpdate);

		OwnerDTO ownerDTOUpdate = this.ownerService.update(newOwnerDTOCreate);
		log.info("{}", ownerDTOUpdate);

		assertEquals(UP_FIRST_NAME, ownerDTOUpdate.getFirstName());
		assertEquals(UP_LAST_NAME,  ownerDTOUpdate.getLastName());
		assertEquals(UP_ADDRESS,    ownerDTOUpdate.getAddress());
		assertEquals(UP_CITY,       ownerDTOUpdate.getCity());
		assertEquals(UP_TELEPHONE,  ownerDTOUpdate.getTelephone());
	}

	// ── ELIMINACION ───────────────────────────────────────────────────────────

	@Test
	public void testDeleteOwner() {

		Owner newOwner        = new Owner(0, "Sofia", "Mendoza", "Jiron Junin 456", "Trujillo", "977666555");
		Owner newOwnerCreated = new Owner(2000, "Sofia", "Mendoza", "Jiron Junin 456", "Trujillo", "977666555");

		OwnerDTO newOwnerDTO = this.ownerMapper.mapToDto(newOwner);

		// ------------ Create ---------------
		Mockito.when(this.repository.save(newOwner)).thenReturn(newOwnerCreated);
		OwnerDTO ownerDTOCreate = this.ownerService.create(newOwnerDTO);
		log.info("{}", ownerDTOCreate);

		// ------------ Delete ---------------
		Mockito.doNothing().when(this.repository).delete(newOwnerCreated);
		Mockito.when(this.repository.findById(newOwnerCreated.getId()))
				.thenReturn(Optional.of(newOwnerCreated));

		try {
			this.ownerService.delete(ownerDTOCreate.getId());
		} catch (OwnerNotFoundException e) {
			fail(e.getMessage());
		}

		// ------------ Validate ---------------
		Mockito.when(this.repository.findById(newOwnerCreated.getId()))
				.thenReturn(Optional.ofNullable(null));

		try {
			this.ownerService.findById(ownerDTOCreate.getId());
			assertTrue(false);
		} catch (OwnerNotFoundException e) {
			assertTrue(true);
		}
	}
}
