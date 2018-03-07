package fr.edjaz.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.web.rest.errors.BadRequestAlertException;
import fr.edjaz.chat.web.rest.util.HeaderUtil;
import fr.edjaz.chat.web.rest.util.PaginationUtil;
import fr.edjaz.chat.service.dto.ConseillerDTO;
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
 * REST controller for managing Conseiller.
 */
@RestController
@RequestMapping("/api")
public class ConseillerResource {

    private final Logger log = LoggerFactory.getLogger(ConseillerResource.class);

    private static final String ENTITY_NAME = "conseiller";

    private final ConseillerService conseillerService;

    public ConseillerResource(ConseillerService conseillerService) {
        this.conseillerService = conseillerService;
    }

    /**
     * POST  /conseillers : Create a new conseiller.
     *
     * @param conseillerDTO the conseillerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conseillerDTO, or with status 400 (Bad Request) if the conseiller has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conseillers")
    @Timed
    public ResponseEntity<ConseillerDTO> createConseiller(@RequestBody ConseillerDTO conseillerDTO) throws URISyntaxException {
        log.debug("REST request to save Conseiller : {}", conseillerDTO);
        if (conseillerDTO.getId() != null) {
            throw new BadRequestAlertException("A new conseiller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConseillerDTO result = conseillerService.save(conseillerDTO);
        return ResponseEntity.created(new URI("/api/conseillers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conseillers : Updates an existing conseiller.
     *
     * @param conseillerDTO the conseillerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conseillerDTO,
     * or with status 400 (Bad Request) if the conseillerDTO is not valid,
     * or with status 500 (Internal Server Error) if the conseillerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conseillers")
    @Timed
    public ResponseEntity<ConseillerDTO> updateConseiller(@RequestBody ConseillerDTO conseillerDTO) throws URISyntaxException {
        log.debug("REST request to update Conseiller : {}", conseillerDTO);
        if (conseillerDTO.getId() == null) {
            return createConseiller(conseillerDTO);
        }
        ConseillerDTO result = conseillerService.save(conseillerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conseillerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conseillers : get all the conseillers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of conseillers in body
     */
    @GetMapping("/conseillers")
    @Timed
    public ResponseEntity<List<ConseillerDTO>> getAllConseillers(Pageable pageable) {
        log.debug("REST request to get a page of Conseillers");
        Page<ConseillerDTO> page = conseillerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conseillers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /conseillers/:id : get the "id" conseiller.
     *
     * @param id the id of the conseillerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conseillerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/conseillers/{id}")
    @Timed
    public ResponseEntity<ConseillerDTO> getConseiller(@PathVariable Long id) {
        log.debug("REST request to get Conseiller : {}", id);
        ConseillerDTO conseillerDTO = conseillerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conseillerDTO));
    }

    /**
     * DELETE  /conseillers/:id : delete the "id" conseiller.
     *
     * @param id the id of the conseillerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conseillers/{id}")
    @Timed
    public ResponseEntity<Void> deleteConseiller(@PathVariable Long id) {
        log.debug("REST request to delete Conseiller : {}", id);
        conseillerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conseillers?query=:query : search for the conseiller corresponding
     * to the query.
     *
     * @param query the query of the conseiller search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/conseillers")
    @Timed
    public ResponseEntity<List<ConseillerDTO>> searchConseillers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Conseillers for query {}", query);
        Page<ConseillerDTO> page = conseillerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/conseillers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
