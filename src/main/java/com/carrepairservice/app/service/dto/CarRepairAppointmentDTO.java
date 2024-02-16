package com.carrepairservice.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.carrepairservice.app.domain.CarRepairAppointment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarRepairAppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private CarDTO car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarRepairAppointmentDTO)) {
            return false;
        }

        CarRepairAppointmentDTO carRepairAppointmentDTO = (CarRepairAppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carRepairAppointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarRepairAppointmentDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", car=" + getCar() +
            "}";
    }
}
