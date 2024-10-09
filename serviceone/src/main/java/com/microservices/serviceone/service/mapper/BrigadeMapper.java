package com.microservices.serviceone.service.mapper;

import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.domain.Governorate;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.dto.GovernorateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brigade} and its DTO {@link BrigadeDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrigadeMapper extends EntityMapper<BrigadeDTO, Brigade> {
    @Mapping(target = "governorate", source = "governorate", qualifiedByName = "governorateId")
    BrigadeDTO toDto(Brigade s);

    @Named("governorateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GovernorateDTO toDtoGovernorateId(Governorate governorate);
}
