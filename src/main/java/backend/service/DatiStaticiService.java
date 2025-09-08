package backend.service;

import backend.dto.dati_statici.CreateOrUpdateStaticDataDTO;
import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.StaticDataMapper;
import backend.repository.DatiStaticiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<ResponseStaticDataDTO> getAllDatas() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public ResponseStaticDataDTO getDataById(UUID dataId) {
        return repository.findById(dataId)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Dati statici non trovati per ID: " + dataId));
    }

    public ResponseStaticDataDTO createData(CreateOrUpdateStaticDataDTO createDTO) {
        var entity = mapper.toEntity(createDTO);
        var savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public ResponseStaticDataDTO updateData(UUID dataId, CreateOrUpdateStaticDataDTO updateDTO) {
        var existingEntity = repository.findById(dataId)
                .orElseThrow(() -> new ResourceNotFoundException("Dati statici non trovati per ID: " + dataId));

        mapper.partialUpdate(updateDTO, existingEntity);
        var updatedEntity = repository.save(existingEntity);
        return mapper.toDto(updatedEntity);
    }

    public Void deleteData(UUID dataId) {
        var existingEntity = repository.findById(dataId)
                .orElseThrow(() -> new ResourceNotFoundException("Dati statici non trovati per ID: " + dataId));

        repository.delete(existingEntity);
        return null;
    }
}
