package backend.mapper;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.model.Prodotto;
import backend.model.Recensione;
import backend.model.Utente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper extends GenericMapper<Recensione, CreateReviewDTO, UpdateReviewDTO, ResponseReviewDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "data", expression = "java(java.time.LocalDate.now())")
    @Mapping(source = "dto.descrizione", target = "descrizione")
    Recensione toEntity(CreateReviewDTO dto, Prodotto prodotto, Utente utente);


    @Mapping(source = "prodotto.nome", target = "nomeProdotto")
    @Mapping(source = "utente.nome", target = "nomeUtente")
    @Mapping(source = "utente.cognome", target = "cognomeUtente")
    ResponseReviewDTO toResponseDto(Recensione recensione);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdateFromCreate(CreateReviewDTO createReviewDTO, @MappingTarget Recensione recensione);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdateFromUpdate(UpdateReviewDTO updateReviewDto, @MappingTarget Recensione recensione);
}