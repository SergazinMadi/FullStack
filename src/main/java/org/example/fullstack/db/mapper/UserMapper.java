package org.example.fullstack.db.mapper;


import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", defaultValue = "CLIENT")
    User registrationRequestToUser(RegistrationRequest registrationRequest);
}