package backend.mapper;

import backend.dto.carrello.UpdateCartItemDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.model.Carrello;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CartMapper extends GenericMapper<Carrello, UpdateCartItemDTO, UpdateCartItemDTO, ResponseCartDTO> {

    @Override
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    Carrello fromCreateDto(UpdateCartItemDTO updateCartItemDTO);

    @Override
    @Mapping(source = "prodotto", target = "prodotto")
    ResponseCartDTO toDto(Carrello carrello);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "prodottoId", target = "id.prodottoId")
    Carrello partialUpdateFromCreate(UpdateCartItemDTO updateCartItemDTO, @MappingTarget Carrello carrello);


}