package fr.edjaz.chat.service.mapper;

import fr.edjaz.chat.domain.*;
import fr.edjaz.chat.service.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {ExtraInformationMapper.class})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    @Mapping(source = "extra.id", target = "extraId")
    ClientDTO toDto(Client client);

    @Mapping(source = "extraId", target = "extra")
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
