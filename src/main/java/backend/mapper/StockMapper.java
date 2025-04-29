package backend.mapper;

import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.model.Stock;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface StockMapper extends GenericMapper<Stock, CreateStockDTO, UpdateStockDTO, ResponseStockDTO> {
    @Override
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock fromCreateDto(CreateStockDTO createStockDTO);

    @Override
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock fromUpdateDto(UpdateStockDTO updateStockDTO);

    @Override
    @Mapping(source = "filiale.nome", target = "filialeNome")
    @Mapping(source = "filiale.id", target = "filialeId")
    ResponseStockDTO toDto(Stock stock);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Stock partialUpdateFromCreate(CreateStockDTO createStockDTO, @MappingTarget Stock stock);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Stock partialUpdateFromUpdate(UpdateStockDTO updateStockDTO, @MappingTarget Stock stock);
}