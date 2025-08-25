package backend.mapper;


import backend.dto.categoria.*;
import backend.model.Categoria;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CategoryMapper extends GenericMapper<Categoria, CreateCategoryDTO, UpdateCategoryDTO, ResponseCategoryDTO> {


    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Categoria partialUpdateFromCreate(CreateCategoryDTO createCategoryDTO, @MappingTarget Categoria categoria);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Categoria partialUpdateFromUpdate(UpdateCategoryDTO updateCategoryDTO, @MappingTarget Categoria categoria);


    @Mapping(target = "isLeaf", expression = "java(categoria.getChildren() == null || categoria.getChildren().isEmpty())")
    ResponseCategoryNavigationDTO toNavigationDto(Categoria categoria);

    ResponseParentCategoryDTO toParentDto(Categoria categoria);
}