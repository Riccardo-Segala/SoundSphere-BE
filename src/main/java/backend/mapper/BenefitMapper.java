package backend.mapper;

import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.model.Vantaggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BenefitMapper {
    Vantaggio toEntity(ResponseBenefitDTO responseBenefitDto);

    ResponseBenefitDTO toDto(Vantaggio vantaggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdate(ResponseBenefitDTO responseBenefitDto, @MappingTarget Vantaggio vantaggio);
}