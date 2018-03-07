package fr.edjaz.chat.repository.search;

import fr.edjaz.chat.domain.Chat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Chat entity.
 */
public interface ChatSearchRepository extends ElasticsearchRepository<Chat, Long> {
}
