package backend.service;

import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.StaticDataMapper;
import backend.repository.DatiStaticiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatiStaticiService {
    private final DatiStaticiRepository repository;
    private final StaticDataMapper mapper;

    public ResponseStaticDataDTO getStaticDataByName(String name) {
        return repository.findByNome(name)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Dati statici non trovati per nome: " + name));
    }
}
