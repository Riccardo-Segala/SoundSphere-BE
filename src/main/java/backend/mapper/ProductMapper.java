package backend.mapper;

import backend.dto.prodotto.UpdateProductDTO;
import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.model.Prodotto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper extends GenericMapper<Prodotto, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {


    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdateFromCreate(CreateProductDTO createProductDTO, @MappingTarget Prodotto prodotto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdateFromUpdate(UpdateProductDTO updateProductDTO, @MappingTarget Prodotto prodotto);
}