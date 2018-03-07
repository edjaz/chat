package fr.edjaz.chat.repository;

import fr.edjaz.chat.domain.ExtraInformation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExtraInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraInformationRepository extends JpaRepository<ExtraInformation, Long> {

}
