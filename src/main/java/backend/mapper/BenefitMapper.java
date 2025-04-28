package backend.mapper;

import backend.dto.vantaggio.CreateBenefitDTO;
import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.dto.vantaggio.UpdateBenefitDTO;
import backend.model.Vantaggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BenefitMapper {
    Vantaggio toEntity(ResponseBenefitDTO responseBenefitDto);

    ResponseBenefitDTO toDto(Vantaggio vantaggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdate(ResponseBenefitDTO responseBenefitDto, @MappingTarget Vantaggio vantaggio);

    Vantaggio toEntity(CreateBenefitDTO createBenefitDTO);

    CreateBenefitDTO toDto1(Vantaggio vantaggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdate(CreateBenefitDTO createBenefitDTO, @MappingTarget Vantaggio vantaggio);

    Vantaggio toEntity(UpdateBenefitDTO updateBenefitDTO);

    UpdateBenefitDTO toDto2(Vantaggio vantaggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdate(UpdateBenefitDTO updateBenefitDTO, @MappingTarget Vantaggio vantaggio);
}