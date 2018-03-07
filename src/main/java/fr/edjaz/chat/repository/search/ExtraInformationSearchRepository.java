package fr.edjaz.chat.repository.search;

import fr.edjaz.chat.domain.ExtraInformation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ExtraInformation entity.
 */
public interface ExtraInformationSearchRepository extends ElasticsearchRepository<ExtraInformation, Long> {
}
