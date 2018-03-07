package fr.edjaz.chat.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Conseiller entity.
 */
public class ConseillerDTO implements Serializable {

    private Long id;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConseillerDTO conseillerDTO = (ConseillerDTO) o;
        if(conseillerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conseillerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConseillerDTO{" +
            "id=" + getId() +
            "}";
    }
}
