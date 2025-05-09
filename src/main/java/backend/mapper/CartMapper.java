package backend.mapper;

import backend.dto.carrello.CreateCartDTO;
import backend.dto.carrello.UpdateCartDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.model.Carrello;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CartMapper extends GenericMapper<Carrello, CreateCartDTO, UpdateCartDTO, ResponseCartDTO> {

    @Override
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello fromCreateDto(CreateCartDTO createCartDTO);

    @Override
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello fromUpdateDto(UpdateCartDTO updateCartDTO);

    @Override
    @Mapping(source = "prodotto", target = "prodotto")
    @Mapping(source = "id.utenteId", target = "utenteId")
    ResponseCartDTO toDto(Carrello carrello);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello partialUpdateFromCreate(CreateCartDTO createCartDTO, @MappingTarget Carrello carrello);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    @Mapping(source = "utenteId", target = "id.utenteId")
    Carrello partialUpdateFromUpdate(UpdateCartDTO updateCartDTO, @MappingTarget Carrello carrello);
}