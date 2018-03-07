package fr.edjaz.chat.repository.search;

import fr.edjaz.chat.domain.Conseiller;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Conseiller entity.
 */
public interface ConseillerSearchRepository extends ElasticsearchRepository<Conseiller, Long> {
}
