package com.carrepairservice.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.carrepairservice.app.domain.CarService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarServiceDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private String address;

    private Set<CarRepairAppointmentDTO> repairAppointments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CarRepairAppointmentDTO> getRepairAppointments() {
        return repairAppointments;
    }

    public void setRepairAppointments(Set<CarRepairAppointmentDTO> repairAppointments) {
        this.repairAppointments = repairAppointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarServiceDTO)) {
            return false;
        }

        CarServiceDTO carServiceDTO = (CarServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarServiceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
