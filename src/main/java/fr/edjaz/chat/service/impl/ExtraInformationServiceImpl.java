package fr.edjaz.chat.service.impl;

import fr.edjaz.chat.service.ExtraInformationService;
import fr.edjaz.chat.domain.ExtraInformation;
import fr.edjaz.chat.repository.ExtraInformationRepository;
import fr.edjaz.chat.repository.search.ExtraInformationSearchRepository;
import fr.edjaz.chat.service.dto.ExtraInformationDTO;
import fr.edjaz.chat.service.mapper.ExtraInformationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ExtraInformation.
 */
@Service
@Transactional
public class ExtraInformationServiceImpl implements ExtraInformationService {

    private final Logger log = LoggerFactory.getLogger(ExtraInformationServiceImpl.class);

    private final ExtraInformationRepository extraInformationRepository;

    private final ExtraInformationMapper extraInformationMapper;

    private final ExtraInformationSearchRepository extraInformationSearchRepository;

    public ExtraInformationServiceImpl(ExtraInformationRepository extraInformationRepository, ExtraInformationMapper extraInformationMapper, ExtraInformationSearchRepository extraInformationSearchRepository) {
        this.extraInformationRepository = extraInformationRepository;
        this.extraInformationMapper = extraInformationMapper;
        this.extraInformationSearchRepository = extraInformationSearchRepository;
    }

    /**
     * Save a extraInformation.
     *
     * @param extraInformationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExtraInformationDTO save(ExtraInformationDTO extraInformationDTO) {
        log.debug("Request to save ExtraInformation : {}", extraInformationDTO);
        ExtraInformation extraInformation = extraInformationMapper.toEntity(extraInformationDTO);
        extraInformation = extraInformationRepository.save(extraInformation);
        ExtraInformationDTO result = extraInformationMapper.toDto(extraInformation);
        extraInformationSearchRepository.save(extraInformation);
        return result;
    }

    /**
     * Get all the extraInformations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExtraInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraInformations");
        return extraInformationRepository.findAll(pageable)
            .map(extraInformationMapper::toDto);
    }

    /**
     * Get one extraInformation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExtraInformationDTO findOne(Long id) {
        log.debug("Request to get ExtraInformation : {}", id);
        ExtraInformation extraInformation = extraInformationRepository.findById(id).get();
        return extraInformationMapper.toDto(extraInformation);
    }

    /**
     * Delete the extraInformation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExtraInformation : {}", id);
        extraInformationRepository.deleteById(id);
        extraInformationSearchRepository.deleteById(id);
    }

    /**
     * Search for the extraInformation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExtraInformationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExtraInformations for query {}", query);
        Page<ExtraInformation> result = extraInformationSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(extraInformationMapper::toDto);
    }
}
