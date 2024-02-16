package com.carrepairservice.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.carrepairservice.app.domain.Car} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarDTO implements Serializable {

    private Long id;

    private String company;

    @Min(value = 1970)
    private Integer manufacturedYear;

    @NotNull
    private String ownerName;

    private CarRepairAppointmentDTO carRepairAppointment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getManufacturedYear() {
        return manufacturedYear;
    }

    public void setManufacturedYear(Integer manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public CarRepairAppointmentDTO getCarRepairAppointment() {
        return carRepairAppointment;
    }

    public void setCarRepairAppointment(CarRepairAppointmentDTO carRepairAppointment) {
        this.carRepairAppointment = carRepairAppointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", manufacturedYear=" + getManufacturedYear() +
            ", ownerName='" + getOwnerName() + "'" +
            ", carRepairAppointment=" + getCarRepairAppointment() +
            "}";
    }
}
