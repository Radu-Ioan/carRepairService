package com.carrepairservice.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarRepairAppointment.
 */
@Entity
@Table(name = "car_repair_appointment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarRepairAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @JsonIgnoreProperties(value = { "carRepairAppointment" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Car car;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "repairAppointments", "employees" }, allowSetters = true)
    private CarService carService;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "repairAppointments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carService", "repairAppointments" }, allowSetters = true)
    private Set<CarServiceEmployee> responsibleEmployees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarRepairAppointment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CarRepairAppointment date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public CarRepairAppointment car(Car car) {
        this.setCar(car);
        return this;
    }

    public CarService getCarService() {
        return this.carService;
    }

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    public CarRepairAppointment carService(CarService carService) {
        this.setCarService(carService);
        return this;
    }

    public Set<CarServiceEmployee> getResponsibleEmployees() {
        return this.responsibleEmployees;
    }

    public void setResponsibleEmployees(Set<CarServiceEmployee> carServiceEmployees) {
        if (this.responsibleEmployees != null) {
            this.responsibleEmployees.forEach(i -> i.removeRepairAppointments(this));
        }
        if (carServiceEmployees != null) {
            carServiceEmployees.forEach(i -> i.addRepairAppointments(this));
        }
        this.responsibleEmployees = carServiceEmployees;
    }

    public CarRepairAppointment responsibleEmployees(Set<CarServiceEmployee> carServiceEmployees) {
        this.setResponsibleEmployees(carServiceEmployees);
        return this;
    }

    public CarRepairAppointment addResponsibleEmployees(CarServiceEmployee carServiceEmployee) {
        this.responsibleEmployees.add(carServiceEmployee);
        carServiceEmployee.getRepairAppointments().add(this);
        return this;
    }

    public CarRepairAppointment removeResponsibleEmployees(CarServiceEmployee carServiceEmployee) {
        this.responsibleEmployees.remove(carServiceEmployee);
        carServiceEmployee.getRepairAppointments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarRepairAppointment)) {
            return false;
        }
        return getId() != null && getId().equals(((CarRepairAppointment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarRepairAppointment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
