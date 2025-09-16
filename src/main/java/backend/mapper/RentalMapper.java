package backend.mapper;

import backend.dto.checkout.CheckoutOutputRentalDTO;
import backend.dto.noleggio.CreateRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.dto.noleggio.UpdateRentalDTO;
import backend.model.Noleggio;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {BigDecimal.class, Instant.class, ZoneOffset.class},
        uses = {RentalDetailsMapper.class, ProductMapper.class})
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

    @Mapping(target = "noleggioId", source = "id")
    @Mapping(target = "numeroNoleggio", source = "id", qualifiedByName = "generateNumeroNoleggio")
    @Mapping(target = "dataScadenza", source = "dataScadenza")
    @Mapping(target = "importoTotale", source = "totale")
    @Mapping(target = "indirizzoSpedizione", source = "indirizzo") // Delega a IndirizzoUtenteMapper
    @Mapping(target = "prodottiNoleggiati", source = "dettagli") // Delega a RentalDetailsMapper
    CheckoutOutputRentalDTO toCheckoutOutputRentalDTO(Noleggio noleggio);

    /**
     * Logica personalizzata per generare un numero di noleggio leggibile,
     * proprio come OrderMapper faceva per il numero d'ordine.
     */
    @Named("generateNumeroNoleggio")
    default String generateNumeroNoleggio(UUID id) {
        if (id == null) {
            return null;
        }
        // Esempio: "RENT-" seguito da parte dell'UUID
        return "RENT-" + id.toString().substring(0, 8).toUpperCase();
    }
}