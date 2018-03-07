package fr.edjaz.chat.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fr.edjaz.chat.domain.enumeration.ChatStatus;

/**
 * A DTO for the Chat entity.
 */
public class ChatDTO implements Serializable {

    private Long id;

    private ChatStatus status;

    private Instant created;

    private Instant opened;

    private Instant closed;

    private Long clientId;

    private Set<ConseillerDTO> conseillers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getOpened() {
        return opened;
    }

    public void setOpened(Instant opened) {
        this.opened = opened;
    }

    public Instant getClosed() {
        return closed;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Set<ConseillerDTO> getConseillers() {
        return conseillers;
    }

    public void setConseillers(Set<ConseillerDTO> conseillers) {
        this.conseillers = conseillers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatDTO chatDTO = (ChatDTO) o;
        if(chatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", opened='" + getOpened() + "'" +
            ", closed='" + getClosed() + "'" +
            "}";
    }
}
