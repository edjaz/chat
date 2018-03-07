package fr.edjaz.chat.service.mapper;

import fr.edjaz.chat.domain.*;
import fr.edjaz.chat.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ChatMapper.class, ClientMapper.class, ConseillerMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "writeByClient.id", target = "writeByClientId")
    @Mapping(source = "writeByConseiller.id", target = "writeByConseillerId")
    MessageDTO toDto(Message message);

    @Mapping(source = "chatId", target = "chat")
    @Mapping(source = "writeByClientId", target = "writeByClient")
    @Mapping(source = "writeByConseillerId", target = "writeByConseiller")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
