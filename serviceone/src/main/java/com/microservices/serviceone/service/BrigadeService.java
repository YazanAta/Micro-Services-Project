package com.microservices.serviceone.service;

import com.microservices.serviceone.domain.Brigade;
import com.microservices.serviceone.repository.BrigadeRepository;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.mapper.BrigadeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.microservices.serviceone.domain.Brigade}.
 */
@Service
@Transactional
public class BrigadeService {

    private static final Logger LOG = LoggerFactory.getLogger(BrigadeService.class);

    private final BrigadeRepository brigadeRepository;

    private final BrigadeMapper brigadeMapper;

    public BrigadeService(BrigadeRepository brigadeRepository, BrigadeMapper brigadeMapper) {
        this.brigadeRepository = brigadeRepository;
        this.brigadeMapper = brigadeMapper;
    }

    /**
     * Save a brigade.
     *
     * @param brigadeDTO the entity to save.
     * @return the persisted entity.
     */
    public BrigadeDTO save(BrigadeDTO brigadeDTO) {
        LOG.debug("Request to save Brigade : {}", brigadeDTO);
        Brigade brigade = brigadeMapper.toEntity(brigadeDTO);
        brigade = brigadeRepository.save(brigade);
        return brigadeMapper.toDto(brigade);
    }

    /**
     * Update a brigade.
     *
     * @param brigadeDTO the entity to save.
     * @return the persisted entity.
     */
    public BrigadeDTO update(BrigadeDTO brigadeDTO) {
        LOG.debug("Request to update Brigade : {}", brigadeDTO);
        Brigade brigade = brigadeMapper.toEntity(brigadeDTO);
        brigade = brigadeRepository.save(brigade);
        return brigadeMapper.toDto(brigade);
    }

    /**
     * Partially update a brigade.
     *
     * @param brigadeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BrigadeDTO> partialUpdate(BrigadeDTO brigadeDTO) {
        LOG.debug("Request to partially update Brigade : {}", brigadeDTO);

        return brigadeRepository
            .findById(brigadeDTO.getId())
            .map(existingBrigade -> {
                brigadeMapper.partialUpdate(existingBrigade, brigadeDTO);

                return existingBrigade;
            })
            .map(brigadeRepository::save)
            .map(brigadeMapper::toDto);
    }

    /**
     * Get all the brigades.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BrigadeDTO> findAll() {
        LOG.debug("Request to get all Brigades");
        return brigadeRepository.findAll().stream().map(brigadeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one brigade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BrigadeDTO> findOne(Long id) {
        LOG.debug("Request to get Brigade : {}", id);
        return brigadeRepository.findById(id).map(brigadeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<List<BrigadeDTO>> findBrigadesByGovernorateId(Long governorateId) {
        List<Brigade> brigades = brigadeRepository.findByGovernorate_Id(governorateId);
        if (brigades.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(brigades.stream()
                .map(brigadeMapper::toDto)
                .collect(Collectors.toList()));
        }
    }

    /**
     * Delete the brigade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Brigade : {}", id);
        brigadeRepository.deleteById(id);
    }
}
