package fr.edjaz.chat.repository;

import fr.edjaz.chat.domain.Chat;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Chat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select distinct chat from Chat chat left join fetch chat.conseillers")
    List<Chat> findAllWithEagerRelationships();

    @Query("select chat from Chat chat left join fetch chat.conseillers where chat.id =:id")
    Chat findOneWithEagerRelationships(@Param("id") Long id);

}
