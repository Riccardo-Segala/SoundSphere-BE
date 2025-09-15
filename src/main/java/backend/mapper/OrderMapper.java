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


    @Mapping(target = "ordineId", source = "ordine.id")
    @Mapping(target = "numeroOrdine", source = "ordine.id", qualifiedByName = "generateNumeroOrdine") // Logica custom
    @Mapping(target = "dataOrdine", source = "ordine.dataAcquisto")
    @Mapping(target = "statoOrdine", source = "ordine.stato")
    @Mapping(target = "importoTotale", source = "ordine.totale") // Converte double in BigDecimal
    @Mapping(target = "indirizzoSpedizione", source = "ordine.indirizzo") // Assumendo che esista un AddressMapper
    @Mapping(target = "prodottiOrdinati", source = "ordine.dettagli")
    @Mapping(target = "puntiTotaliUtente", source = "puntiTotaliUtente")
    CheckoutOutputDTO toCheckoutOutputDTO(Ordine ordine, int puntiTotaliUtente);

    @Named("generateNumeroOrdine")
    default String generateNumeroOrdine(UUID id) {
        if (id == null) {
            return null;
        }
        // prende le prime 12 cifre dell'UUID
        return id.toString().substring(0, 12).toUpperCase();
    }
}