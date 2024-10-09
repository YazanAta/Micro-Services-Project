package com.microservices.serviceone.service;

import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.domain.Municipality;
import com.microservices.serviceone.repository.MunicipalityRepository;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.dto.MunicipalityDTO;
import com.microservices.serviceone.service.mapper.MunicipalityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.microservices.serviceone.domain.Municipality}.
 */
@Service
@Transactional
public class MunicipalityService {

    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityService.class);

    private final MunicipalityRepository municipalityRepository;

    private final MunicipalityMapper municipalityMapper;

    public MunicipalityService(MunicipalityRepository municipalityRepository, MunicipalityMapper municipalityMapper) {
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    /**
     * Save a municipality.
     *
     * @param municipalityDTO the entity to save.
     * @return the persisted entity.
     */
    public MunicipalityDTO save(MunicipalityDTO municipalityDTO) {
        LOG.debug("Request to save Municipality : {}", municipalityDTO);
        Municipality municipality = municipalityMapper.toEntity(municipalityDTO);
        municipality = municipalityRepository.save(municipality);
        return municipalityMapper.toDto(municipality);
    }

    /**
     * Update a municipality.
     *
     * @param municipalityDTO the entity to save.
     * @return the persisted entity.
     */
    public MunicipalityDTO update(MunicipalityDTO municipalityDTO) {
        LOG.debug("Request to update Municipality : {}", municipalityDTO);
        Municipality municipality = municipalityMapper.toEntity(municipalityDTO);
        municipality = municipalityRepository.save(municipality);
        return municipalityMapper.toDto(municipality);
    }

    /**
     * Partially update a municipality.
     *
     * @param municipalityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MunicipalityDTO> partialUpdate(MunicipalityDTO municipalityDTO) {
        LOG.debug("Request to partially update Municipality : {}", municipalityDTO);

        return municipalityRepository
            .findById(municipalityDTO.getId())
            .map(existingMunicipality -> {
                municipalityMapper.partialUpdate(existingMunicipality, municipalityDTO);

                return existingMunicipality;
            })
            .map(municipalityRepository::save)
            .map(municipalityMapper::toDto);
    }

    /**
     * Get all the municipalities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MunicipalityDTO> findAll() {
        LOG.debug("Request to get all Municipalities");
        return municipalityRepository.findAll().stream().map(municipalityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one municipality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MunicipalityDTO> findOne(Long id) {
        LOG.debug("Request to get Municipality : {}", id);
        return municipalityRepository.findById(id).map(municipalityMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<List<MunicipalityDTO>> findMunicipalityByBrigadeId(Long brigadeId) {
        List<Municipality> municipalities = municipalityRepository.findByBrigade_Id(brigadeId);
        if (municipalities.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(municipalities.stream()
                .map(municipalityMapper::toDto)
                .collect(Collectors.toList()));
        }
    }

    /**
     * Delete the municipality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Municipality : {}", id);
        municipalityRepository.deleteById(id);
    }
}
