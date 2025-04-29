package backend.mapper;

import org.mapstruct.MappingTarget;

public interface GenericMapper<T, CreateDTO, UpdateDTO, ResponseDTO> {
    ResponseDTO toDto(T entity);
    T fromCreateDto(CreateDTO createDTO);
    T fromUpdateDto(UpdateDTO updateDTO);
    T partialUpdateFromCreate(CreateDTO createDTO, @MappingTarget T entity);
    T partialUpdateFromUpdate(UpdateDTO updateDTO, @MappingTarget T entity);
}