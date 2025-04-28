package backend.mapper;

import backend.dto.prodotto.UpdateProductDTO;
import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.model.Prodotto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Prodotto toEntity(CreateProductDTO createProductDTO);

    CreateProductDTO toDto(Prodotto prodotto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdate(CreateProductDTO createProductDTO, @MappingTarget Prodotto prodotto);

    Prodotto toEntity(UpdateProductDTO updateProductDTO);

    UpdateProductDTO toDto1(Prodotto prodotto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdate(UpdateProductDTO updateProductDTO, @MappingTarget Prodotto prodotto);

    // Metodo per mappare tutti i campi di Prodotto a ResponseProductDTO
    ResponseProductDTO toResponseProductDto(Prodotto prodotto);
}