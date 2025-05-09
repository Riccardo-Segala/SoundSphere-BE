package backend.mapper;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.model.Filiale;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper extends GenericMapper<Filiale, CreateBranchDTO, UpdateBranchDTO, ResponseBranchDTO> {

    @Override
    ResponseBranchDTO toDto(Filiale filiale);

    @Override
    Filiale fromCreateDto(CreateBranchDTO createBranchDTO);

    @Override
    Filiale fromUpdateDto(UpdateBranchDTO updateBranchDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdateFromCreate(CreateBranchDTO createBranchDTO, @MappingTarget Filiale filiale);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdateFromUpdate(UpdateBranchDTO updateBranchDTO, @MappingTarget Filiale filiale);
}