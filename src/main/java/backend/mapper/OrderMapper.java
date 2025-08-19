package backend.mapper;

import backend.dto.checkout.CheckoutOutputDTO;
import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.model.Ordine;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {BigDecimal.class, Instant.class, ZoneOffset.class},
        uses = OrderDetailsMapper.class)
public interface OrderMapper extends GenericMapper<Ordine, CreateOrderDTO, UpdateOrderDTO, ResponseOrderDTO> {

    @Override
    Ordine fromCreateDto(CreateOrderDTO createOrderDTO);

    @Override
    @Mapping(source = "indirizzoId", target = "indirizzo.id")
    Ordine fromUpdateDto(UpdateOrderDTO updateOrderDTO);

    @Override
    ResponseOrderDTO toDto(Ordine ordine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdateFromCreate(CreateOrderDTO createOrderDTO, @MappingTarget Ordine ordine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdateFromUpdate(UpdateOrderDTO updateOrderDTO, @MappingTarget Ordine ordine);


    @Mapping(target = "ordineId", source = "id")
    @Mapping(target = "numeroOrdine", source = "id", qualifiedByName = "generateNumeroOrdine") // Logica custom
    @Mapping(target = "dataOrdine", source = "dataAcquisto")
    @Mapping(target = "statoOrdine", source = "stato")
    @Mapping(target = "importoTotale", source = "totale") // Converte double in BigDecimal
    @Mapping(target = "indirizzoSpedizione", source = "indirizzo") // Assumendo che esista un AddressMapper
    @Mapping(target = "dataConsegnaStimata", source = "dataConsegna")
    @Mapping(target = "prodottiOrdinati", source = "dettagli")
    CheckoutOutputDTO toCheckoutOutputDTO(Ordine ordine);

    @Named("generateNumeroOrdine")
    default String generateNumeroOrdine(UUID id) {
        if (id == null) {
            return null;
        }
        // prende le prime 12 cifre dell'UUID
        return id.toString().substring(0, 12).toUpperCase();
    }
}