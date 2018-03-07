package fr.edjaz.chat.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ExtraInformation entity.
 */
public class ExtraInformationDTO implements Serializable {

    private Long id;

    private String extras;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtraInformationDTO extraInformationDTO = (ExtraInformationDTO) o;
        if(extraInformationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), extraInformationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExtraInformationDTO{" +
            "id=" + getId() +
            ", extras='" + getExtras() + "'" +
            "}";
    }
}
