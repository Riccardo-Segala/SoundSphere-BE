package backend.mapper;

import backend.dto.dati_statici.CreateOrUpdateStaticDataDTO;
import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.model.DatiStatici;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaticDataMapper {
    DatiStatici toEntity(ResponseStaticDataDTO responseStaticDataDTO);

    ResponseStaticDataDTO toDto(DatiStatici datiStatici);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DatiStatici partialUpdate(ResponseStaticDataDTO responseStaticDataDTO, @MappingTarget DatiStatici datiStatici);

    DatiStatici toEntity(CreateOrUpdateStaticDataDTO createOrUpdateStaticDataDTO);

    CreateOrUpdateStaticDataDTO toCreateOrUpdateDTO(DatiStatici datiStatici);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DatiStatici partialUpdate(CreateOrUpdateStaticDataDTO createOrUpdateStaticDataDTO, @MappingTarget DatiStatici datiStatici);
}