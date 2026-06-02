package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final TypeMapper     typeMapper;

    public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper     = typeMapper;
    }

    @Override
    public TypeDTO create(TypeDTO typeDTO) {
        Type saved = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(saved);
    }

    @Override
    public TypeDTO update(TypeDTO typeDTO) {
        Type saved = typeRepository.save(typeMapper.mapToEntity(typeDTO));
        return typeMapper.mapToDto(saved);
    }

    @Override
    public void delete(Integer id) throws TypeNotFoundException {
        TypeDTO dto = findById(id);
        typeRepository.delete(typeMapper.mapToEntity(dto));
    }

    @Override
    public TypeDTO findById(Integer id) throws TypeNotFoundException {
        Optional<Type> type = typeRepository.findById(id);
        if (!type.isPresent())
            throw new TypeNotFoundException("Type not found with id: " + id);
        return typeMapper.mapToDto(type.get());
    }

    @Override
    public List<TypeDTO> findByName(String name) {
        List<Type> types = typeRepository.findByName(name);
        types.forEach(t -> log.info("" + t));
        return types.stream()
                .map(typeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Type> findAll() {
        return typeRepository.findAll();
    }
}
