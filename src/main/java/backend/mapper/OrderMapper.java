package backend.mapper;

import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.model.Ordine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Ordine toEntity(ResponseOrderDTO responseOrderDTO);

    ResponseOrderDTO toDto(Ordine ordine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdate(ResponseOrderDTO responseOrderDTO, @MappingTarget Ordine ordine);

    Ordine toEntity(CreateOrderDTO createOrderDTO);

    CreateOrderDTO toDto1(Ordine ordine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdate(CreateOrderDTO createOrderDTO, @MappingTarget Ordine ordine);

    @Mapping(source = "indirizzoId", target = "indirizzo.id")
    Ordine toEntity(UpdateOrderDTO updateOrderDTO);

    @Mapping(source = "indirizzo.id", target = "indirizzoId")
    UpdateOrderDTO toDto2(Ordine ordine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "indirizzoId", target = "indirizzo.id")
    Ordine partialUpdate(UpdateOrderDTO updateOrderDTO, @MappingTarget Ordine ordine);
}