package fr.edjaz.chat.repository.search;

import fr.edjaz.chat.domain.Client;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Client entity.
 */
public interface ClientSearchRepository extends ElasticsearchRepository<Client, Long> {
}
