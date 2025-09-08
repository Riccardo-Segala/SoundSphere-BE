package backend.mapper;

import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.dto.stock.admin.UpdateStockFromAdminDTO;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.model.Stock;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface StockMapper extends GenericMapper<Stock, CreateStockDTO, UpdateStockDTO, ResponseStockDTO> {
    @Override
    @Mapping(source = "filialeId", target = "filiale.id")
    Stock fromCreateDto(CreateStockDTO createStockDTO);

    @Override
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateFromAdminUpdate(UpdateStockFromAdminDTO updateAdminDTO, @MappingTarget Stock stock);


    @Mapping(target = "id", ignore = true) // Ignora l'ID perché è una nuova entità
    @Mapping(target = "quantita", constant = "0") // Imposta la quantità a 0
    @Mapping(target = "quantitaPerNoleggio", constant = "0") // Imposta la quantità per noleggio a 0
    @Mapping(target = "prodotto", source = "prodotto") // MapStruct lo farebbe in automatico, ma è bene essere espliciti
    @Mapping(target = "filiale", source = "filiale") // MapStruct lo farebbe in automatico
    Stock createStockFromNewBranchOrProduct(Prodotto prodotto, Filiale filiale);
}