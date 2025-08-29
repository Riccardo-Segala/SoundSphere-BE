package backend.mapper;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.model.MetodoPagamento;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMethodMapper extends GenericMapper<MetodoPagamento, CreatePaymentMethodDTO, UpdatePaymentMethodDTO, ResponsePaymentMethodDTO> {

    @Override
    MetodoPagamento fromCreateDto(CreatePaymentMethodDTO createPaymentMethodDTO);

    @Override
    ResponsePaymentMethodDTO toDto(MetodoPagamento metodoPagamento);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MetodoPagamento partialUpdateFromCreate(CreatePaymentMethodDTO createPaymentMethodDTO, @MappingTarget MetodoPagamento metodoPagamento);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MetodoPagamento partialUpdateFromUpdate(UpdatePaymentMethodDTO updatePaymentMethodDTO, @MappingTarget MetodoPagamento metodoPagamento);

    // Metodo aggiuntivo per mappare ResponsePaymentMethodDTO a MetodoPagamento
    MetodoPagamento fromUpdateDto(UpdatePaymentMethodDTO updatePaymentMethodDTO);
}