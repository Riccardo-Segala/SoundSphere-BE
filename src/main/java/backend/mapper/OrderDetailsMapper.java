package backend.mapper;

import backend.dto.dettagli_ordine.CreateOrderDetailsDTO;
import backend.dto.dettagli_ordine.ResponseOrderDetailsDTO;
import backend.dto.dettagli_ordine.UpdateOrderDetailsDTO;
import backend.model.DettagliOrdine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface
OrderDetailsMapper {
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine toEntity(ResponseOrderDetailsDTO responseOrderDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseOrderDetailsDTO toDto(DettagliOrdine dettagliOrdine);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliOrdine partialUpdate(ResponseOrderDetailsDTO responseOrderDetailsDTO, @MappingTarget DettagliOrdine dettagliOrdine);

    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine toEntity(CreateOrderDetailsDTO createOrderDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateOrderDetailsDTO toDto1(DettagliOrdine dettagliOrdine);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliOrdine partialUpdate(CreateOrderDetailsDTO createOrderDetailsDTO, @MappingTarget DettagliOrdine dettagliOrdine);

    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "ordineId", target = "ordine.id")
    DettagliOrdine toEntity(UpdateOrderDetailsDTO updateOrderDetailsDTO);

    @InheritInverseConfiguration(name = "toEntity")
    UpdateOrderDetailsDTO toDto2(DettagliOrdine dettagliOrdine);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DettagliOrdine partialUpdate(UpdateOrderDetailsDTO updateOrderDetailsDTO, @MappingTarget DettagliOrdine dettagliOrdine);
}