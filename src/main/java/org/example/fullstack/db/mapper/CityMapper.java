package org.example.fullstack.db.mapper;

import org.example.fullstack.db.dto.dto.CityDto;
import org.example.fullstack.db.dto.request.CityCreateRequest;
import org.example.fullstack.db.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {
    City cityCreateRequestToCity(CityCreateRequest request);
    CityDto cityToCityDto(City city);
}
