package backend.mapper;

import backend.dto.organizzatore_eventi.CreateEventManagerDTO;
import backend.dto.organizzatore_eventi.ResponseEventManagerDTO;
import backend.dto.organizzatore_eventi.UpdateEventManagerDTO;
import backend.model.OrganizzatoreEventi;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventManagerMapper extends GenericMapper<OrganizzatoreEventi, CreateEventManagerDTO, UpdateEventManagerDTO, ResponseEventManagerDTO> {

    @Override
    OrganizzatoreEventi fromCreateDto(CreateEventManagerDTO createEventManagerDTO);

    @Override
    OrganizzatoreEventi fromUpdateDto(UpdateEventManagerDTO updateEventManagerDTO);

    @Override
    @Mapping(source = "vantaggio.id", target = "vantaggioId")
    ResponseEventManagerDTO toDto(OrganizzatoreEventi organizzatoreEventi);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizzatoreEventi partialUpdateFromCreate(CreateEventManagerDTO createEventManagerDTO, @MappingTarget OrganizzatoreEventi organizzatoreEventi);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizzatoreEventi partialUpdateFromUpdate(UpdateEventManagerDTO updateEventManagerDTO, @MappingTarget OrganizzatoreEventi organizzatoreEventi);
}