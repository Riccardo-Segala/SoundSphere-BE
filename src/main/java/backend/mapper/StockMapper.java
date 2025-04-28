package backend.mapper;

import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.model.Stock;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface StockMapper {
    @Mapping(source = "filialeNome", target = "filiale.nome")
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock toEntity(ResponseStockDTO responseStockDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseStockDTO toDto(Stock stock);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Stock partialUpdate(ResponseStockDTO responseStockDTO, @MappingTarget Stock stock);

    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock toEntity(CreateStockDTO createStockDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateStockDTO toDto1(Stock stock);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Stock partialUpdate(CreateStockDTO createStockDTO, @MappingTarget Stock stock);

    @Mapping(source = "prodottoId", target = "prodotto.id")
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock toEntity(UpdateStockDTO updateStockDTO);

    @InheritInverseConfiguration(name = "toEntity")
    UpdateStockDTO toDto2(Stock stock);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Stock partialUpdate(UpdateStockDTO updateStockDTO, @MappingTarget Stock stock);
}