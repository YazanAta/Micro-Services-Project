package com.microservices.serviceone.service;

import com.microservices.serviceone.domain.Governorate;
import com.microservices.serviceone.repository.GovernorateRepository;
import com.microservices.serviceone.service.dto.GovernorateDTO;
import com.microservices.serviceone.service.mapper.GovernorateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.microservices.serviceone.domain.Governorate}.
 */
@Service
@Transactional
public class GovernorateService {

    private static final Logger LOG = LoggerFactory.getLogger(GovernorateService.class);

    private final GovernorateRepository governorateRepository;

    private final GovernorateMapper governorateMapper;

    public GovernorateService(GovernorateRepository governorateRepository, GovernorateMapper governorateMapper) {
        this.governorateRepository = governorateRepository;
        this.governorateMapper = governorateMapper;
    }

    /**
     * Save a governorate.
     *
     * @param governorateDTO the entity to save.
     * @return the persisted entity.
     */
    public GovernorateDTO save(GovernorateDTO governorateDTO) {
        LOG.debug("Request to save Governorate : {}", governorateDTO);
        Governorate governorate = governorateMapper.toEntity(governorateDTO);
        governorate = governorateRepository.save(governorate);
        return governorateMapper.toDto(governorate);
    }

    /**
     * Update a governorate.
     *
     * @param governorateDTO the entity to save.
     * @return the persisted entity.
     */
    public GovernorateDTO update(GovernorateDTO governorateDTO) {
        LOG.debug("Request to update Governorate : {}", governorateDTO);
        Governorate governorate = governorateMapper.toEntity(governorateDTO);
        governorate = governorateRepository.save(governorate);
        return governorateMapper.toDto(governorate);
    }

    /**
     * Partially update a governorate.
     *
     * @param governorateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GovernorateDTO> partialUpdate(GovernorateDTO governorateDTO) {
        LOG.debug("Request to partially update Governorate : {}", governorateDTO);

        return governorateRepository
            .findById(governorateDTO.getId())
            .map(existingGovernorate -> {
                governorateMapper.partialUpdate(existingGovernorate, governorateDTO);

                return existingGovernorate;
            })
            .map(governorateRepository::save)
            .map(governorateMapper::toDto);
    }

    /**
     * Get all the governorates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GovernorateDTO> findAll() {
        LOG.debug("Request to get all Governorates");
        return governorateRepository.findAll().stream().map(governorateMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one governorate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GovernorateDTO> findOne(Long id) {
        LOG.debug("Request to get Governorate : {}", id);
        return governorateRepository.findById(id).map(governorateMapper::toDto);
    }

    /**
     * Delete the governorate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Governorate : {}", id);
        governorateRepository.deleteById(id);
    }
}
