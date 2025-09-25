package backend.controller;

import backend.dto.dati_statici.DeliveryLimitDataDTO;
import backend.service.DatiStaticiService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path="/api/dati-statici", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatiStaticiController {

    private final DatiStaticiService datiStaticiService;

    @GetMapping("/delivery-cost")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryLimitDataDTO> getDeliveryDataAndLimits() {

        DeliveryLimitDataDTO data = datiStaticiService.getDeliveryDataAndLimits();
        return ResponseEntity.ok(data);

    }
}
