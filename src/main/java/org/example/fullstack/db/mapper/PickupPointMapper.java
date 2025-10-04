package org.example.fullstack.db.mapper;

import org.example.fullstack.db.dto.dto.PickupPointDto;
import org.example.fullstack.db.dto.request.PickupPointCreateRequest;
import org.example.fullstack.db.dto.request.PickupPointUpdateRequest;
import org.example.fullstack.db.model.PickupPoint;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PickupPointMapper {
    PickupPoint pickupPointCreateRequestToPickupPoint(PickupPointCreateRequest request);
    PickupPointDto pickupPointToPickupPointDto(PickupPoint pickupPoint);
    void updatePickupPointFromRequest(PickupPointUpdateRequest request, @org.mapstruct.MappingTarget PickupPoint pickupPoint);
}
