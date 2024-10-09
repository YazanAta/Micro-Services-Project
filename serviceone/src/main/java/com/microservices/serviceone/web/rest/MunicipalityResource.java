package com.microservices.serviceone.web.rest;

import com.microservices.serviceone.repository.MunicipalityRepository;
import com.microservices.serviceone.service.MunicipalityService;
import com.microservices.serviceone.service.dto.BrigadeDTO;
import com.microservices.serviceone.service.dto.MunicipalityDTO;
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
 * REST controller for managing {@link com.microservices.serviceone.domain.Municipality}.
 */
@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityResource {

    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityResource.class);

    private static final String ENTITY_NAME = "serviceoneMunicipality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MunicipalityService municipalityService;

    private final MunicipalityRepository municipalityRepository;

    public MunicipalityResource(MunicipalityService municipalityService, MunicipalityRepository municipalityRepository) {
        this.municipalityService = municipalityService;
        this.municipalityRepository = municipalityRepository;
    }

    /**
     * {@code POST  /municipalities} : Create a new municipality.
     *
     * @param municipalityDTO the municipalityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new municipalityDTO, or with status {@code 400 (Bad Request)} if the municipality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MunicipalityDTO> createMunicipality(@Valid @RequestBody MunicipalityDTO municipalityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Municipality : {}", municipalityDTO);
        if (municipalityDTO.getId() != null) {
            throw new BadRequestAlertException("A new municipality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        municipalityDTO = municipalityService.save(municipalityDTO);
        return ResponseEntity.created(new URI("/api/municipalities/" + municipalityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, municipalityDTO.getId().toString()))
            .body(municipalityDTO);
    }
    @GetMapping("/brigade/{id}")
    public ResponseEntity<List<MunicipalityDTO>> getMunicipalityByBrigadeId(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Municipality by Brigades : {}", id);
        Optional<List<MunicipalityDTO>> municipalityDTOS = municipalityService.findMunicipalityByBrigadeId(id);
        return ResponseUtil.wrapOrNotFound(municipalityDTOS);
    }
    /**
     * {@code PUT  /municipalities/:id} : Updates an existing municipality.
     *
     * @param id the id of the municipalityDTO to save.
     * @param municipalityDTO the municipalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipalityDTO,
     * or with status {@code 400 (Bad Request)} if the municipalityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the municipalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> updateMunicipality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MunicipalityDTO municipalityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Municipality : {}, {}", id, municipalityDTO);
        if (municipalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        municipalityDTO = municipalityService.update(municipalityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipalityDTO.getId().toString()))
            .body(municipalityDTO);
    }

    /**
     * {@code PATCH  /municipalities/:id} : Partial updates given fields of an existing municipality, field will ignore if it is null
     *
     * @param id the id of the municipalityDTO to save.
     * @param municipalityDTO the municipalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipalityDTO,
     * or with status {@code 400 (Bad Request)} if the municipalityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the municipalityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the municipalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MunicipalityDTO> partialUpdateMunicipality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MunicipalityDTO municipalityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Municipality partially : {}, {}", id, municipalityDTO);
        if (municipalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MunicipalityDTO> result = municipalityService.partialUpdate(municipalityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipalityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /municipalities} : get all the municipalities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of municipalities in body.
     */
    @GetMapping("")
    public List<MunicipalityDTO> getAllMunicipalities() {
        LOG.debug("REST request to get all Municipalities");
        return municipalityService.findAll();
    }

    /**
     * {@code GET  /municipalities/:id} : get the "id" municipality.
     *
     * @param id the id of the municipalityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the municipalityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> getMunicipality(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Municipality : {}", id);
        Optional<MunicipalityDTO> municipalityDTO = municipalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(municipalityDTO);
    }

    /**
     * {@code DELETE  /municipalities/:id} : delete the "id" municipality.
     *
     * @param id the id of the municipalityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMunicipality(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Municipality : {}", id);
        municipalityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
