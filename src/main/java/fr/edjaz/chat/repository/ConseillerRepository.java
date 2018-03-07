package fr.edjaz.chat.repository;

import fr.edjaz.chat.domain.Conseiller;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Conseiller entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConseillerRepository extends JpaRepository<Conseiller, Long> {

}
