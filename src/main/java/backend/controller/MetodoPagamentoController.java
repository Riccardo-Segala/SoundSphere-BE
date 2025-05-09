package backend.controller;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.mapper.PaymentMethodMapper;
import backend.model.MetodoPagamento;
import backend.service.MetodoPagamentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/metodi-pagamento")
class MetodoPagamentoController extends GenericController<MetodoPagamento, UUID, CreatePaymentMethodDTO, UpdatePaymentMethodDTO, ResponsePaymentMethodDTO> {
    public MetodoPagamentoController(MetodoPagamentoService service, PaymentMethodMapper mapper) {
        super(service, mapper);
    }
}
