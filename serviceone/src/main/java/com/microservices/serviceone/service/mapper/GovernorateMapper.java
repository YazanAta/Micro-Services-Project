package com.microservices.serviceone.service.mapper;

import com.microservices.serviceone.domain.Governorate;
import com.microservices.serviceone.service.dto.GovernorateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Governorate} and its DTO {@link GovernorateDTO}.
 */
@Mapper(componentModel = "spring")
public interface GovernorateMapper extends EntityMapper<GovernorateDTO, Governorate> {}
