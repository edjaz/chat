package fr.edjaz.chat.service.mapper;

import fr.edjaz.chat.domain.*;
import fr.edjaz.chat.service.dto.ConseillerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Conseiller and its DTO ConseillerDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ConseillerMapper extends EntityMapper<ConseillerDTO, Conseiller> {

    @Mapping(source = "user.id", target = "userId")
    ConseillerDTO toDto(Conseiller conseiller);

    @Mapping(source = "userId", target = "user")
    Conseiller toEntity(ConseillerDTO conseillerDTO);

    default Conseiller fromId(Long id) {
        if (id == null) {
            return null;
        }
        Conseiller conseiller = new Conseiller();
        conseiller.setId(id);
        return conseiller;
    }
}
