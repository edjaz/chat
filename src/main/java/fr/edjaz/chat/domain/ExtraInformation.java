package fr.edjaz.chat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExtraInformation.
 */
@Entity
@Table(name = "extra_information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "extrainformation")
public class ExtraInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "extras")
    private String extras;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtras() {
        return extras;
    }

    public ExtraInformation extras(String extras) {
        this.extras = extras;
        return this;
    }

    public void setExtras(String extras) {
        this.extras = extras;
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
        ExtraInformation extraInformation = (ExtraInformation) o;
        if (extraInformation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), extraInformation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExtraInformation{" +
            "id=" + getId() +
            ", extras='" + getExtras() + "'" +
            "}";
    }
}
