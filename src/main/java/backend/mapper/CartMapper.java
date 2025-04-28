package backend.mapper;

import backend.dto.carrello.CreateCartDTO;
import backend.dto.carrello.UpdateCartDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.model.Carrello;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CartMapper {

    // Mapping per CreateCartDTO -> Carrello
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello toEntity(CreateCartDTO createCartDTO);

    // Mapping inverso: Carrello -> CreateCartDTO
    @InheritInverseConfiguration(name = "toEntity")
    CreateCartDTO toCreateDto(Carrello carrello);

    // Mapping per UpdateCartDTO -> Carrello
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello toEntity(UpdateCartDTO updateCartDTO);

    // Mapping inverso: Carrello -> UpdateCartDTO
    @InheritInverseConfiguration(name = "toEntity")
    UpdateCartDTO toUpdateDto(Carrello carrello);

    // Mapping per Carrello -> ResponseCartDTO
    @Mapping(source = "prodotto", target = "prodotto")
    @Mapping(source = "id.utenteId", target = "utenteId")
    ResponseCartDTO toResponseDto(Carrello carrello);

    // Aggiornamento parziale con UpdateCartDTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello partialUpdate(UpdateCartDTO updateCartDTO, @MappingTarget Carrello carrello);

    // Aggiornamento parziale con ResponseCartDTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello partialUpdate(ResponseCartDTO responseCartDTO, @MappingTarget Carrello carrello);
}