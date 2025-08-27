package backend.mapper;

import backend.dto.filiale.BranchAddressDTO;
import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.model.Filiale;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {


    @Mapping(target = ".", source = "indirizzo")
    ResponseBranchDTO toDto(Filiale filiale);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "indirizzo", ignore = true)
    Filiale fromCreateDto(CreateBranchDTO createBranchDTO);

    @Mapping(target = "id", ignore = true) // Ignora sempre l'ID durante un aggiornamento
    @Mapping(target = "indirizzo", ignore = true)
    Filiale fromUpdateDto(UpdateBranchDTO updateBranchDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdateFromCreate(CreateBranchDTO createBranchDTO, @MappingTarget Filiale filiale);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Filiale partialUpdateFromUpdate(UpdateBranchDTO updateBranchDTO, @MappingTarget Filiale filiale);

    default BranchAddressDTO toIndirizzoScomposto(String indirizzoCompleto) {
        if (indirizzoCompleto == null || indirizzoCompleto.isBlank()) {
            return new BranchAddressDTO(null, null, null, null, null);
        }

        String[] parts = indirizzoCompleto.split(",");

        // Gestisce il caso in cui il formato della stringa sia quello atteso
        if (parts.length == 5) {
            return new BranchAddressDTO(
                    parts[0].trim(), // via
                    parts[1].trim(), // cap
                    parts[2].trim(), // citta
                    parts[3].trim(), // provincia
                    parts[4].trim()  // nazione
            );
        }

        // Caso di fallback se il formato non è corretto
        return new BranchAddressDTO(indirizzoCompleto, null, null, null, null);
    }

    @AfterMapping
    default void joinIndirizzoFromCreateToEntity(CreateBranchDTO dto, @MappingTarget Filiale filiale) {
        String indirizzo = String.join(", ",
                dto.via(),
                dto.citta(),
                dto.cap(),
                dto.provincia(),
                dto.nazione()
        );
        filiale.setIndirizzo(indirizzo);
    }

    @AfterMapping
    default void joinIndirizzoFromUpdateToEntity(UpdateBranchDTO dto, @MappingTarget Filiale filiale) {
        String indirizzo = String.join(", ",
                dto.via(),
                dto.citta(),
                dto.cap(),
                dto.provincia(),
                dto.nazione()
        );
        filiale.setIndirizzo(indirizzo);
    }
}