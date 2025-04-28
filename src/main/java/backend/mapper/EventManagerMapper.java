package backend.mapper;

import backend.dto.organizzatore_eventi.CreateEventManagerDTO;
import backend.dto.organizzatore_eventi.ResponseEventManagerDTO;
import backend.dto.organizzatore_eventi.UpdateEventManagerDTO;
import backend.model.OrganizzatoreEventi;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventManagerMapper {
    OrganizzatoreEventi toEntity(CreateEventManagerDTO createEventManagerDTO);

    CreateEventManagerDTO toDto(OrganizzatoreEventi organizzatoreEventi);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizzatoreEventi partialUpdate(CreateEventManagerDTO createEventManagerDTO, @MappingTarget OrganizzatoreEventi organizzatoreEventi);

    OrganizzatoreEventi toEntity(UpdateEventManagerDTO updateEventManagerDTO);

    UpdateEventManagerDTO toDto1(OrganizzatoreEventi organizzatoreEventi);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrganizzatoreEventi partialUpdate(UpdateEventManagerDTO updateEventManagerDTO, @MappingTarget OrganizzatoreEventi organizzatoreEventi);

    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    OrganizzatoreEventi toEntity(ResponseEventManagerDTO responseEventManagerDTO);

    @Mapping(source = "vantaggio.id", target = "vantaggioId")
    ResponseEventManagerDTO toDto2(OrganizzatoreEventi organizzatoreEventi);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    OrganizzatoreEventi partialUpdate(ResponseEventManagerDTO responseEventManagerDTO, @MappingTarget OrganizzatoreEventi organizzatoreEventi);
}