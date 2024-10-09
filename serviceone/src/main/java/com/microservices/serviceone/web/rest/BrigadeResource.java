package com.microservices.serviceone.web.rest;

import com.microservices.serviceone.repository.BrigadeRepository;
import com.microservices.serviceone.service.BrigadeService;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.microservices.serviceone.domain.Brigade}.
 */
@RestController
@RequestMapping("/api/brigades")
public class BrigadeResource {

    private static final Logger LOG = LoggerFactory.getLogger(BrigadeResource.class);

    private static final String ENTITY_NAME = "serviceoneBrigade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BrigadeService brigadeService;

    private final BrigadeRepository brigadeRepository;

    public BrigadeResource(BrigadeService brigadeService, BrigadeRepository brigadeRepository) {
        this.brigadeService = brigadeService;
        this.brigadeRepository = brigadeRepository;
    }

    /**
     * {@code POST  /brigades} : Create a new brigade.
     *
     * @param brigadeDTO the brigadeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brigadeDTO, or with status {@code 400 (Bad Request)} if the brigade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BrigadeDTO> createBrigade(@Valid @RequestBody BrigadeDTO brigadeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Brigade : {}", brigadeDTO);
        if (brigadeDTO.getId() != null) {
            throw new BadRequestAlertException("A new brigade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        brigadeDTO = brigadeService.save(brigadeDTO);
        return ResponseEntity.created(new URI("/api/brigades/" + brigadeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, brigadeDTO.getId().toString()))
            .body(brigadeDTO);
    }

    /**
     * {@code PUT  /brigades/:id} : Updates an existing brigade.
     *
     * @param id the id of the brigadeDTO to save.
     * @param brigadeDTO the brigadeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brigadeDTO,
     * or with status {@code 400 (Bad Request)} if the brigadeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brigadeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrigadeDTO> updateBrigade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BrigadeDTO brigadeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Brigade : {}, {}", id, brigadeDTO);
        if (brigadeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brigadeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brigadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        brigadeDTO = brigadeService.update(brigadeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brigadeDTO.getId().toString()))
            .body(brigadeDTO);
    }

    /**
     * {@code PATCH  /brigades/:id} : Partial updates given fields of an existing brigade, field will ignore if it is null
     *
     * @param id the id of the brigadeDTO to save.
     * @param brigadeDTO the brigadeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brigadeDTO,
     * or with status {@code 400 (Bad Request)} if the brigadeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the brigadeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the brigadeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BrigadeDTO> partialUpdateBrigade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BrigadeDTO brigadeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Brigade partially : {}, {}", id, brigadeDTO);
        if (brigadeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brigadeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!brigadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BrigadeDTO> result = brigadeService.partialUpdate(brigadeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brigadeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /brigades} : get all the brigades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brigades in body.
     */
    @GetMapping("")
    public List<BrigadeDTO> getAllBrigades() {
        LOG.debug("REST request to get all Brigades");
        return brigadeService.findAll();
    }

    /**
     * {@code GET  /brigades/:id} : get the "id" brigade.
     *
     * @param id the id of the brigadeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brigadeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrigadeDTO> getBrigade(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Brigade : {}", id);
        Optional<BrigadeDTO> brigadeDTO = brigadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brigadeDTO);
    }

    /**
     * {@code GET  /brigades/:id} : get the "id" brigade.
     *
     * @param id the id of the brigadeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brigadeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/governorate/{id}")
    public ResponseEntity<List<BrigadeDTO>> getBrigadesByGovernorateId(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Brigades by Governorate : {}", id);
        Optional<List<BrigadeDTO>> brigadeDTOs = brigadeService.findBrigadesByGovernorateId(id);
        return ResponseUtil.wrapOrNotFound(brigadeDTOs);
    }

    /**
     * {@code DELETE  /brigades/:id} : delete the "id" brigade.
     *
     * @param id the id of the brigadeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrigade(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Brigade : {}", id);
        brigadeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
