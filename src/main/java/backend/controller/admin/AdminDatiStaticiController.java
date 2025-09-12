package backend.controller.admin;

import backend.dto.dati_statici.CreateOrUpdateStaticDataDTO;
import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.service.DatiStaticiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/admin/dati-statici", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDatiStaticiController {
    private final DatiStaticiService datiStaticiService;
    @GetMapping
    public ResponseEntity<List<ResponseStaticDataDTO>> getAllDatas() {
        return ResponseEntity.ok(datiStaticiService.getAllDatas());
    }

    @GetMapping("/{dataId}")
    public ResponseEntity<ResponseStaticDataDTO> getDataById(@PathVariable UUID dataId) {
        return ResponseEntity.ok(datiStaticiService.getDataById(dataId));
    }

    @PostMapping
    public ResponseEntity<ResponseStaticDataDTO> createData(@RequestBody CreateOrUpdateStaticDataDTO createDTO) {
        return ResponseEntity.ok(datiStaticiService.createData(createDTO));
    }

    @PutMapping("/{dataId}")
    public ResponseEntity<ResponseStaticDataDTO> updateData(@PathVariable UUID dataId, @RequestBody CreateOrUpdateStaticDataDTO updateDTO) {
        return ResponseEntity.ok(datiStaticiService.updateData(dataId, updateDTO));
    }

    @DeleteMapping("/{dataId}")
    public ResponseEntity<Void> deleteData(@PathVariable UUID dataId) {
        return ResponseEntity.ok(datiStaticiService.deleteData(dataId));
    }
}
