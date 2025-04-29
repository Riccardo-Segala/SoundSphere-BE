package backend.mapper;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.model.MetodoPagamento;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMethodMapper {
    @Mapping(source = "utenteId", target = "utente.id")
    MetodoPagamento toEntity(CreatePaymentMethodDTO createPaymentMethodDTO);

    @Mapping(source = "utente.id", target = "utenteId")
    CreatePaymentMethodDTO toDto(MetodoPagamento metodoPagamento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "utenteId", target = "utente.id")
    MetodoPagamento partialUpdate(CreatePaymentMethodDTO createPaymentMethodDTO, @MappingTarget MetodoPagamento metodoPagamento);

    MetodoPagamento partialUpdate(UpdatePaymentMethodDTO updatePaymentMethodDTO, @MappingTarget MetodoPagamento metodoPagamento);

    MetodoPagamento toEntity(ResponsePaymentMethodDTO responsePaymentMethodDTO);

    ResponsePaymentMethodDTO toDto2(MetodoPagamento metodoPagamento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MetodoPagamento partialUpdate(ResponsePaymentMethodDTO responsePaymentMethodDTO, @MappingTarget MetodoPagamento metodoPagamento);
}