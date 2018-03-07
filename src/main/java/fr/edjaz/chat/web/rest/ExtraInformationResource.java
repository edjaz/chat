package fr.edjaz.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.edjaz.chat.service.ExtraInformationService;
import fr.edjaz.chat.web.rest.errors.BadRequestAlertException;
import fr.edjaz.chat.web.rest.util.HeaderUtil;
import fr.edjaz.chat.web.rest.util.PaginationUtil;
import fr.edjaz.chat.service.dto.ExtraInformationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ExtraInformation.
 */
@RestController
@RequestMapping("/api")
public class ExtraInformationResource {

    private final Logger log = LoggerFactory.getLogger(ExtraInformationResource.class);

    private static final String ENTITY_NAME = "extraInformation";

    private final ExtraInformationService extraInformationService;

    public ExtraInformationResource(ExtraInformationService extraInformationService) {
        this.extraInformationService = extraInformationService;
    }

    /**
     * POST  /extra-informations : Create a new extraInformation.
     *
     * @param extraInformationDTO the extraInformationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraInformationDTO, or with status 400 (Bad Request) if the extraInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-informations")
    @Timed
    public ResponseEntity<ExtraInformationDTO> createExtraInformation(@RequestBody ExtraInformationDTO extraInformationDTO) throws URISyntaxException {
        log.debug("REST request to save ExtraInformation : {}", extraInformationDTO);
        if (extraInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new extraInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtraInformationDTO result = extraInformationService.save(extraInformationDTO);
        return ResponseEntity.created(new URI("/api/extra-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extra-informations : Updates an existing extraInformation.
     *
     * @param extraInformationDTO the extraInformationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraInformationDTO,
     * or with status 400 (Bad Request) if the extraInformationDTO is not valid,
     * or with status 500 (Internal Server Error) if the extraInformationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extra-informations")
    @Timed
    public ResponseEntity<ExtraInformationDTO> updateExtraInformation(@RequestBody ExtraInformationDTO extraInformationDTO) throws URISyntaxException {
        log.debug("REST request to update ExtraInformation : {}", extraInformationDTO);
        if (extraInformationDTO.getId() == null) {
            return createExtraInformation(extraInformationDTO);
        }
        ExtraInformationDTO result = extraInformationService.save(extraInformationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, extraInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extra-informations : get all the extraInformations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraInformations in body
     */
    @GetMapping("/extra-informations")
    @Timed
    public ResponseEntity<List<ExtraInformationDTO>> getAllExtraInformations(Pageable pageable) {
        log.debug("REST request to get a page of ExtraInformations");
        Page<ExtraInformationDTO> page = extraInformationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-informations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extra-informations/:id : get the "id" extraInformation.
     *
     * @param id the id of the extraInformationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraInformationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/extra-informations/{id}")
    @Timed
    public ResponseEntity<ExtraInformationDTO> getExtraInformation(@PathVariable Long id) {
        log.debug("REST request to get ExtraInformation : {}", id);
        ExtraInformationDTO extraInformationDTO = extraInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(extraInformationDTO));
    }

    /**
     * DELETE  /extra-informations/:id : delete the "id" extraInformation.
     *
     * @param id the id of the extraInformationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extra-informations/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtraInformation(@PathVariable Long id) {
        log.debug("REST request to delete ExtraInformation : {}", id);
        extraInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/extra-informations?query=:query : search for the extraInformation corresponding
     * to the query.
     *
     * @param query the query of the extraInformation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/extra-informations")
    @Timed
    public ResponseEntity<List<ExtraInformationDTO>> searchExtraInformations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ExtraInformations for query {}", query);
        Page<ExtraInformationDTO> page = extraInformationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/extra-informations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
