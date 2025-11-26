package org.example.fullstack.db.mapper;


import org.example.fullstack.db.dto.dto.ProductDto;
import org.example.fullstack.db.dto.request.ProductCreateRequest;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.model.Product;
import org.example.fullstack.db.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductMapper.class)
public interface ProductMapper {

    @Mapping(target = "receiver", source = "receiverId", qualifiedByName = "mapReceiver")
    Product productCreateRequestToProduct(ProductCreateRequest request);
    ProductDto productToProductDto(Product product);

    @Named("mapReceiver")
    default User mapReceiver(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }
}