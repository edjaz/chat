package fr.edjaz.chat.service.mapper;

import fr.edjaz.chat.domain.*;
import fr.edjaz.chat.service.dto.ExtraInformationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ExtraInformation and its DTO ExtraInformationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExtraInformationMapper extends EntityMapper<ExtraInformationDTO, ExtraInformation> {



    default ExtraInformation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExtraInformation extraInformation = new ExtraInformation();
        extraInformation.setId(id);
        return extraInformation;
    }
}
