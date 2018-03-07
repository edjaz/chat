package fr.edjaz.chat.service;

import fr.edjaz.chat.service.dto.ConseillerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Conseiller.
 */
public interface ConseillerService {

    /**
     * Save a conseiller.
     *
     * @param conseillerDTO the entity to save
     * @return the persisted entity
     */
    ConseillerDTO save(ConseillerDTO conseillerDTO);

    /**
     * Get all the conseillers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConseillerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" conseiller.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ConseillerDTO findOne(Long id);

    /**
     * Delete the "id" conseiller.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the conseiller corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConseillerDTO> search(String query, Pageable pageable);
}
