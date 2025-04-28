package backend.mapper;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.model.Filiale;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {
    Filiale toEntity(ResponseBranchDTO responseBranchDTO);

    ResponseBranchDTO toDto(Filiale filiale);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdate(ResponseBranchDTO responseBranchDTO, @MappingTarget Filiale filiale);

    Filiale toEntity(CreateBranchDTO createBranchDTO);

    CreateBranchDTO toDto1(Filiale filiale);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdate(CreateBranchDTO createBranchDTO, @MappingTarget Filiale filiale);

    Filiale toEntity(UpdateBranchDTO updateBranchDTO);

    UpdateBranchDTO toDto2(Filiale filiale);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdate(UpdateBranchDTO updateBranchDTO, @MappingTarget Filiale filiale);
}