package com.microservices.serviceone.service.mapper;

import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.domain.Municipality;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.dto.MunicipalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Municipality} and its DTO {@link MunicipalityDTO}.
 */
@Mapper(componentModel = "spring")
public interface MunicipalityMapper extends EntityMapper<MunicipalityDTO, Municipality> {
    @Mapping(target = "brigade", source = "brigade", qualifiedByName = "brigadeId")
    MunicipalityDTO toDto(Municipality s);

    @Named("brigadeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BrigadeDTO toDtoBrigadeId(Brigade brigade);
}
