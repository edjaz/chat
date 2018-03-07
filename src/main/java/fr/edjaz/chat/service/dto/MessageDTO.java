package fr.edjaz.chat.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fr.edjaz.chat.domain.enumeration.MessageStatus;

/**
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String text;

    private MessageStatus status;

    private Instant created;

    private Instant updated;

    private Instant sent;

    private Long chatId;

    private Long writeByClientId;

    private Long writeByConseillerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getSent() {
        return sent;
    }

    public void setSent(Instant sent) {
        this.sent = sent;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getWriteByClientId() {
        return writeByClientId;
    }

    public void setWriteByClientId(Long clientId) {
        this.writeByClientId = clientId;
    }

    public Long getWriteByConseillerId() {
        return writeByConseillerId;
    }

    public void setWriteByConseillerId(Long conseillerId) {
        this.writeByConseillerId = conseillerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if(messageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), messageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", sent='" + getSent() + "'" +
            "}";
    }
}
