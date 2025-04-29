package backend.mapper;

import backend.dto.dettagli_noleggio.CreateRentalDetailsDTO;
import backend.dto.dettagli_noleggio.ResponseRentalDetailsDTO;
import backend.dto.dettagli_noleggio.UpdateRentalDetailsDTO;
import backend.model.DettagliNoleggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalDetailsMapper {
    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio toEntity(CreateRentalDetailsDTO createRentalDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateRentalDetailsDTO toDto(DettagliNoleggio dettagliNoleggio);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliNoleggio partialUpdate(CreateRentalDetailsDTO createRentalDetailsDTO, @MappingTarget DettagliNoleggio dettagliNoleggio);

    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio toEntity(UpdateRentalDetailsDTO updateRentalDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    UpdateRentalDetailsDTO toDto1(DettagliNoleggio dettagliNoleggio);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliNoleggio partialUpdate(UpdateRentalDetailsDTO updateRentalDetailsDTO, @MappingTarget DettagliNoleggio dettagliNoleggio);

    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoPathImmagine", target = "prodotto.pathImmagine")
    @Mapping(source = "prodottoCostoGiornaliero", target = "prodotto.costoGiornaliero")
    @Mapping(source = "prodottoDescrizione", target = "prodotto.descrizione")
    @Mapping(source = "prodottoNome", target = "prodotto.nome")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio toEntity(ResponseRentalDetailsDTO responseRentalDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseRentalDetailsDTO toDto2(DettagliNoleggio dettagliNoleggio);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliNoleggio partialUpdate(ResponseRentalDetailsDTO responseRentalDetailsDTO, @MappingTarget DettagliNoleggio dettagliNoleggio);
}