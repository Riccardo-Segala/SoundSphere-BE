package backend.mapper;

import backend.dto.dettagli_noleggio.CreateRentalDetailsDTO;
import backend.dto.dettagli_noleggio.ResponseRentalDetailsDTO;
import backend.dto.dettagli_noleggio.UpdateRentalDetailsDTO;
import backend.model.DettagliNoleggio;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalDetailsMapper extends GenericMapper<DettagliNoleggio, CreateRentalDetailsDTO, UpdateRentalDetailsDTO, ResponseRentalDetailsDTO> {

    @Override
    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio fromCreateDto(CreateRentalDetailsDTO createRentalDetailsDTO);

    @Override
    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio fromUpdateDto(UpdateRentalDetailsDTO updateRentalDetailsDTO);

    @Override
    @Mapping(source = "organizzatoreEventi.id", target = "organizzatoreEventiId")
    @Mapping(source = "prodotto.id", target = "prodottoId")
    @Mapping(source = "noleggio.id", target = "noleggioId")
    ResponseRentalDetailsDTO toDto(DettagliNoleggio dettagliNoleggio);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio partialUpdateFromCreate(CreateRentalDetailsDTO createRentalDetailsDTO, @MappingTarget DettagliNoleggio dettagliNoleggio);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "organizzatoreEventiId", target = "organizzatoreEventi.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "noleggioId", target = "noleggio.id")
    DettagliNoleggio partialUpdateFromUpdate(UpdateRentalDetailsDTO updateRentalDetailsDTO, @MappingTarget DettagliNoleggio dettagliNoleggio);
}