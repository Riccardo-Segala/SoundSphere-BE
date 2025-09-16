package backend.mapper;

import backend.dto.prodotto.CatalogProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.model.Prodotto;
import backend.model.Stock;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class})
public interface ProductMapper extends GenericMapper<Prodotto, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {


    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdateFromCreate(CreateProductDTO createProductDTO, @MappingTarget Prodotto prodotto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Prodotto partialUpdateFromUpdate(UpdateProductDTO updateProductDTO, @MappingTarget Prodotto prodotto);

    @Mapping(target = "id", source = "prodotto.id")
    @Mapping(target = "nome", source = "prodotto.nome")
    @Mapping(target = "marca", source = "prodotto.marca")
    @Mapping(target = "prezzo", source = "prodotto.prezzo")
    @Mapping(target = "pathImmagine", source = "prodotto.pathImmagine")
    @Mapping(target = "stelleMedie", source = "stelleMedie")
    @Mapping(target = "costoGiornaliero", source = "prodotto.costoGiornaliero")
    @Mapping(target = "quantitaDisponibile", expression = "java(stock == null ? 0 : stock.getQuantita())")
    @Mapping(target = "quantitaDisponibileAlNoleggio", expression = "java(stock == null ? 0 : stock.getQuantitaPerNoleggio())")
    CatalogProductDTO toCatalogDTO(Prodotto prodotto, Stock stock, double stelleMedie);
}