package backend.controller;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.mapper.PaymentMethodMapper;
import backend.model.MetodoPagamento;
import backend.security.CustomUserDetails;
import backend.service.MetodoPagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/metodi-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
class MetodoPagamentoController extends GenericController<MetodoPagamento, UUID, CreatePaymentMethodDTO, UpdatePaymentMethodDTO, ResponsePaymentMethodDTO> {

    @Autowired
    private MetodoPagamentoService metodoPagamentoService;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    public MetodoPagamentoController(MetodoPagamentoService service, PaymentMethodMapper mapper) {
        super(service, mapper);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePaymentMethodDTO> getPaymentMethodById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePaymentMethodDTO>> getAllUserPaymentMethod(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId();

        List<ResponsePaymentMethodDTO> methodItems = metodoPagamentoService.getAllPaymentMethodItemsByUserId(userId);

        return ResponseEntity.ok(methodItems);
    }

    @PostMapping
    public ResponseEntity<ResponsePaymentMethodDTO> createPaymentMethod(@Valid @RequestBody CreatePaymentMethodDTO dto) {
        MetodoPagamento savedEntity = metodoPagamentoService.create(dto);
        ResponsePaymentMethodDTO responseDto = paymentMethodMapper.toDto(savedEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedEntity.getId()).toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePaymentMethodDTO> updatePaymentMethod(@PathVariable UUID id, @RequestBody UpdatePaymentMethodDTO updateDTO) {

        MetodoPagamento metodoAggiornato = metodoPagamentoService.update(id, updateDTO);

        return ResponseEntity.ok(paymentMethodMapper.toDto(metodoAggiornato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable UUID id) {
        // Delega tutta la logica complessa al service
        metodoPagamentoService.delete(id);

        // Se il service non lancia eccezioni, la cancellazione è andata a buon fine.
        // Restituisci 204 No Content come da standard per una DELETE.
        return ResponseEntity.noContent().build();
    }

    @Override
    protected UUID getId(MetodoPagamento entity) {
        return entity.getId();
    }
}
