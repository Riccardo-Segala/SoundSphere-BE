package backend.controller;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.mapper.PaymentMethodMapper;
import backend.model.MetodoPagamento;
import backend.service.MetodoPagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/metodi-pagamento")
class MetodoPagamentoController extends GenericController<MetodoPagamento, UUID, CreatePaymentMethodDTO, UpdatePaymentMethodDTO, ResponsePaymentMethodDTO> {
    public MetodoPagamentoController(MetodoPagamentoService service, PaymentMethodMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePaymentMethodDTO>> getAllPaymentMethod() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePaymentMethodDTO> getPaymentMethodById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponsePaymentMethodDTO> createPaymentMethod(@RequestBody CreatePaymentMethodDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePaymentMethodDTO> updatePaymentMethod(@PathVariable UUID id, @RequestBody UpdatePaymentMethodDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(MetodoPagamento entity) {
        return entity.getId();
    }
}
