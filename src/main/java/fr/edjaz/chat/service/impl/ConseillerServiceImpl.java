package fr.edjaz.chat.service.impl;

import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.domain.Conseiller;
import fr.edjaz.chat.repository.ConseillerRepository;
import fr.edjaz.chat.repository.search.ConseillerSearchRepository;
import fr.edjaz.chat.service.dto.ConseillerDTO;
import fr.edjaz.chat.service.mapper.ConseillerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Conseiller.
 */
@Service
@Transactional
public class ConseillerServiceImpl implements ConseillerService {

    private final Logger log = LoggerFactory.getLogger(ConseillerServiceImpl.class);

    private final ConseillerRepository conseillerRepository;

    private final ConseillerMapper conseillerMapper;

    private final ConseillerSearchRepository conseillerSearchRepository;

    public ConseillerServiceImpl(ConseillerRepository conseillerRepository, ConseillerMapper conseillerMapper, ConseillerSearchRepository conseillerSearchRepository) {
        this.conseillerRepository = conseillerRepository;
        this.conseillerMapper = conseillerMapper;
        this.conseillerSearchRepository = conseillerSearchRepository;
    }

    /**
     * Save a conseiller.
     *
     * @param conseillerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ConseillerDTO save(ConseillerDTO conseillerDTO) {
        log.debug("Request to save Conseiller : {}", conseillerDTO);
        Conseiller conseiller = conseillerMapper.toEntity(conseillerDTO);
        conseiller = conseillerRepository.save(conseiller);
        ConseillerDTO result = conseillerMapper.toDto(conseiller);
        conseillerSearchRepository.save(conseiller);
        return result;
    }

    /**
     * Get all the conseillers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConseillerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conseillers");
        return conseillerRepository.findAll(pageable)
            .map(conseillerMapper::toDto);
    }

    /**
     * Get one conseiller by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ConseillerDTO findOne(Long id) {
        log.debug("Request to get Conseiller : {}", id);
        Conseiller conseiller = conseillerRepository.findById(id).get();
        return conseillerMapper.toDto(conseiller);
    }

    /**
     * Delete the conseiller by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conseiller : {}", id);
        conseillerRepository.deleteById(id);
        conseillerSearchRepository.deleteById(id);
    }

    /**
     * Search for the conseiller corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConseillerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Conseillers for query {}", query);
        Page<Conseiller> result = conseillerSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(conseillerMapper::toDto);
    }
}
