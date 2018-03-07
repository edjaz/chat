package fr.edjaz.chat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.edjaz.chat.domain.enumeration.ChatStatus;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "chat")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChatStatus status;

    @Column(name = "created")
    private Instant created;

    @Column(name = "opened")
    private Instant opened;

    @Column(name = "closed")
    private Instant closed;

    @ManyToOne
    private Client client;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "chat_conseiller",
               joinColumns = @JoinColumn(name="chats_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="conseillers_id", referencedColumnName="id"))
    private Set<Conseiller> conseillers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public Chat status(ChatStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }

    public Instant getCreated() {
        return created;
    }

    public Chat created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getOpened() {
        return opened;
    }

    public Chat opened(Instant opened) {
        this.opened = opened;
        return this;
    }

    public void setOpened(Instant opened) {
        this.opened = opened;
    }

    public Instant getClosed() {
        return closed;
    }

    public Chat closed(Instant closed) {
        this.closed = closed;
        return this;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Client getClient() {
        return client;
    }

    public Chat client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Conseiller> getConseillers() {
        return conseillers;
    }

    public Chat conseillers(Set<Conseiller> conseillers) {
        this.conseillers = conseillers;
        return this;
    }

    public Chat addConseiller(Conseiller conseiller) {
        this.conseillers.add(conseiller);
        return this;
    }

    public Chat removeConseiller(Conseiller conseiller) {
        this.conseillers.remove(conseiller);
        return this;
    }

    public void setConseillers(Set<Conseiller> conseillers) {
        this.conseillers = conseillers;
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
        Chat chat = (Chat) o;
        if (chat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", opened='" + getOpened() + "'" +
            ", closed='" + getClosed() + "'" +
            "}";
    }
}
