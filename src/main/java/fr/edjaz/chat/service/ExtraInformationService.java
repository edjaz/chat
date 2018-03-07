package fr.edjaz.chat.service;

import fr.edjaz.chat.service.dto.ExtraInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ExtraInformation.
 */
public interface ExtraInformationService {

    /**
     * Save a extraInformation.
     *
     * @param extraInformationDTO the entity to save
     * @return the persisted entity
     */
    ExtraInformationDTO save(ExtraInformationDTO extraInformationDTO);

    /**
     * Get all the extraInformations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExtraInformationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" extraInformation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ExtraInformationDTO findOne(Long id);

    /**
     * Delete the "id" extraInformation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the extraInformation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExtraInformationDTO> search(String query, Pageable pageable);
}
