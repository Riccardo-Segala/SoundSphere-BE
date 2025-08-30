package backend.mapper;

import backend.dto.vantaggio.CreateBenefitDTO;
import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.dto.vantaggio.UpdateBenefitDTO;
import backend.model.Vantaggio;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BenefitMapper extends GenericMapper<Vantaggio, CreateBenefitDTO, UpdateBenefitDTO, ResponseBenefitDTO> {

    @Override
    ResponseBenefitDTO toDto(Vantaggio vantaggio);

    @Override
    Vantaggio fromCreateDto(CreateBenefitDTO createBenefitDTO);

    @Override
    Vantaggio fromUpdateDto(UpdateBenefitDTO updateBenefitDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdateFromCreate(CreateBenefitDTO createBenefitDTO, @MappingTarget Vantaggio vantaggio);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdateFromUpdate(UpdateBenefitDTO updateBenefitDTO, @MappingTarget Vantaggio vantaggio);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Vantaggio partialUpdate(ResponseBenefitDTO responseBenefitDto, @MappingTarget Vantaggio vantaggio);

    default UUID fromBenefit(Vantaggio benefit) {
        return benefit != null ? benefit.getId() : null;
    }
}