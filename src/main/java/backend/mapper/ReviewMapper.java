package backend.mapper;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.model.Recensione;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    Recensione toEntity(CreateReviewDTO createReviewDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateReviewDTO toDto(Recensione recensione);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdate(CreateReviewDTO createReviewDTO, @MappingTarget Recensione recensione);

    Recensione toEntity(UpdateReviewDTO updateReviewDto);

    UpdateReviewDTO toDto1(Recensione recensione);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdate(UpdateReviewDTO updateReviewDto, @MappingTarget Recensione recensione);

    @Mapping(source = "utenteCognome", target = "utente.cognome")
    @Mapping(source = "utenteNome", target = "utente.nome")
    @Mapping(source = "prodottoNome", target = "prodotto.nome")
    Recensione toEntity(ResponseReviewDTO responseReviewDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseReviewDTO toDto2(Recensione recensione);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdate(ResponseReviewDTO responseReviewDTO, @MappingTarget Recensione recensione);
}