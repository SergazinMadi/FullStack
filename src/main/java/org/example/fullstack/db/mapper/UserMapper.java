package org.example.fullstack.db.mapper;

import org.example.fullstack.db.dto.dto.UserDto;
import org.example.fullstack.db.dto.request.RegistrationRequest;
import org.example.fullstack.db.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User registrationRequestToUser(RegistrationRequest registrationRequest);
    
    UserDto userToUserDto(User user);
}