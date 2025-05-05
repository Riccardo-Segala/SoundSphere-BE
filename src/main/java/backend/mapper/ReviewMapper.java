package backend.mapper;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.model.Recensione;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper extends GenericMapper<Recensione, CreateReviewDTO, UpdateReviewDTO, ResponseReviewDTO> {

    @Override
    @Mapping(source = "utenteId", target = "utente.id")
    @Mapping(source = "prodottoId", target = "prodotto.id")
    Recensione fromCreateDto(CreateReviewDTO createReviewDTO);

    @Override
    Recensione fromUpdateDto(UpdateReviewDTO updateReviewDto);

    @Override
    ResponseReviewDTO toDto(Recensione recensione);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdateFromCreate(CreateReviewDTO createReviewDTO, @MappingTarget Recensione recensione);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recensione partialUpdateFromUpdate(UpdateReviewDTO updateReviewDto, @MappingTarget Recensione recensione);
}