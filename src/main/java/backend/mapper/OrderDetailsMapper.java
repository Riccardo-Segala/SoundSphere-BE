package backend.mapper;

import backend.dto.checkout.ProductOrderOutputDTO;
import backend.dto.dettagli_ordine.CreateOrderDetailsDTO;
import backend.dto.dettagli_ordine.ResponseOrderDetailsDTO;
import backend.dto.dettagli_ordine.UpdateOrderDetailsDTO;
import backend.model.DettagliOrdine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface OrderDetailsMapper extends GenericMapper<DettagliOrdine, CreateOrderDetailsDTO, UpdateOrderDetailsDTO, ResponseOrderDetailsDTO> {

    @Override
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine fromCreateDto(CreateOrderDetailsDTO createOrderDetailsDTO);

    @Override
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine fromUpdateDto(UpdateOrderDetailsDTO updateOrderDetailsDTO);

    @Override
    @Mapping(source = "utente.id", target = "utenteId")
    @Mapping(source = "ordine.id", target = "ordineId")
    ResponseOrderDetailsDTO toDto(DettagliOrdine dettagliOrdine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine partialUpdateFromCreate(CreateOrderDetailsDTO createOrderDetailsDTO, @MappingTarget DettagliOrdine dettagliOrdine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine partialUpdateFromUpdate(UpdateOrderDetailsDTO updateOrderDetailsDTO, @MappingTarget DettagliOrdine dettagliOrdine);

    @Mapping(target = "prodottoId", source = "prodotto.id")
    @Mapping(target = "nomeProdotto", source = "prodotto.nome")
        // 'quantita' viene mappato automaticamente
    ProductOrderOutputDTO toProductOrderOutputDTO(DettagliOrdine dettaglio);
}