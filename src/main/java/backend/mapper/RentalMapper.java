package backend.mapper;

import backend.dto.noleggio.CreateRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.dto.noleggio.UpdateRentalDTO;
import backend.model.Noleggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalMapper {
    Noleggio toEntity(CreateRentalDTO createRentalDTO);

    CreateRentalDTO toDto(Noleggio noleggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Noleggio partialUpdate(CreateRentalDTO createRentalDTO, @MappingTarget Noleggio noleggio);

    Noleggio toEntity(UpdateRentalDTO updateRentalDTO);

    UpdateRentalDTO toDto1(Noleggio noleggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Noleggio partialUpdate(UpdateRentalDTO updateRentalDTO, @MappingTarget Noleggio noleggio);

    Noleggio toEntity(ResponseRentalDTO responseRentalDTO);

    ResponseRentalDTO toDto2(Noleggio noleggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Noleggio partialUpdate(ResponseRentalDTO responseRentalDTO, @MappingTarget Noleggio noleggio);
}