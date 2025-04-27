package backend.mapper;

import backend.dto.carrello.ResponseCartDTO;
import backend.dto.carrello.UpdateCartDTO;
import backend.model.Carrello;
import backend.dto.carrello.CreateCartDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "utenteId", target = "utente.id")
    Carrello toEntity(CreateCartDTO createCartDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateCartDTO toDto(Carrello carrello);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Carrello partialUpdate(CreateCartDTO createCartDTO, @MappingTarget Carrello carrello);

    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "utenteId", target = "utente.id")
    Carrello toEntity(UpdateCartDTO updateCartDto);

    @InheritInverseConfiguration(name = "toEntity")
    UpdateCartDTO toDto1(Carrello carrello);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Carrello partialUpdate(UpdateCartDTO updateCartDto, @MappingTarget Carrello carrello);

    @Mapping(source = "utenteId", target = "utente.id")
    Carrello toEntity(ResponseCartDTO responseCartDto);

    @Mapping(source = "utente.id", target = "utenteId")
    ResponseCartDTO toDto2(Carrello carrello);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "utenteId", target = "utente.id")
    Carrello partialUpdate(ResponseCartDTO responseCartDto, @MappingTarget Carrello carrello);
}