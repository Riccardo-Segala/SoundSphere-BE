package backend.mapper;

import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.model.Ordine;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper extends GenericMapper<Ordine, CreateOrderDTO, UpdateOrderDTO, ResponseOrderDTO> {

    @Override
    Ordine fromCreateDto(CreateOrderDTO createOrderDTO);

    @Override
    @Mapping(source = "indirizzoId", target = "indirizzo.id")
    Ordine fromUpdateDto(UpdateOrderDTO updateOrderDTO);

    @Override
    ResponseOrderDTO toDto(Ordine ordine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdateFromCreate(CreateOrderDTO createOrderDTO, @MappingTarget Ordine ordine);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ordine partialUpdateFromUpdate(UpdateOrderDTO updateOrderDTO, @MappingTarget Ordine ordine);
}