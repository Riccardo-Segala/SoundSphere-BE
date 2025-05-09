package backend.mapper;

import backend.dto.noleggio.CreateRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.dto.noleggio.UpdateRentalDTO;
import backend.model.Noleggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalMapper extends GenericMapper<Noleggio, CreateRentalDTO, UpdateRentalDTO, ResponseRentalDTO> {

    @Override
    Noleggio fromCreateDto(CreateRentalDTO createRentalDTO);

    @Override
    Noleggio fromUpdateDto(UpdateRentalDTO updateRentalDTO);

    @Override
    ResponseRentalDTO toDto(Noleggio noleggio);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Noleggio partialUpdateFromCreate(CreateRentalDTO createRentalDTO, @MappingTarget Noleggio noleggio);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Noleggio partialUpdateFromUpdate(UpdateRentalDTO updateRentalDTO, @MappingTarget Noleggio noleggio);
}