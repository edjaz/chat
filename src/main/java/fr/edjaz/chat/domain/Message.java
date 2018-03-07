package fr.edjaz.chat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import fr.edjaz.chat.domain.enumeration.MessageStatus;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "sent")
    private Instant sent;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private Client writeByClient;

    @ManyToOne
    private Conseiller writeByConseiller;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Message text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Message status(MessageStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Instant getCreated() {
        return created;
    }

    public Message created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public Message updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getSent() {
        return sent;
    }

    public Message sent(Instant sent) {
        this.sent = sent;
        return this;
    }

    public void setSent(Instant sent) {
        this.sent = sent;
    }

    public Chat getChat() {
        return chat;
    }

    public Message chat(Chat chat) {
        this.chat = chat;
        return this;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Client getWriteByClient() {
        return writeByClient;
    }

    public Message writeByClient(Client client) {
        this.writeByClient = client;
        return this;
    }

    public void setWriteByClient(Client client) {
        this.writeByClient = client;
    }

    public Conseiller getWriteByConseiller() {
        return writeByConseiller;
    }

    public Message writeByConseiller(Conseiller conseiller) {
        this.writeByConseiller = conseiller;
        return this;
    }

    public void setWriteByConseiller(Conseiller conseiller) {
        this.writeByConseiller = conseiller;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", sent='" + getSent() + "'" +
            "}";
    }
}
