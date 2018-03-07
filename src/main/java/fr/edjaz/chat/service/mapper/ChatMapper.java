package fr.edjaz.chat.service.mapper;

import fr.edjaz.chat.domain.*;
import fr.edjaz.chat.service.dto.ChatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chat and its DTO ChatDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, ConseillerMapper.class})
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {

    @Mapping(source = "client.id", target = "clientId")
    ChatDTO toDto(Chat chat);

    @Mapping(source = "clientId", target = "client")
    Chat toEntity(ChatDTO chatDTO);

    default Chat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }
}
