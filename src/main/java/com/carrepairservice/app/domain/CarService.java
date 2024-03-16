package com.carrepairservice.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarService.
 */
@Entity
@Table(name = "car_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car", "carService" }, allowSetters = true)
    private Set<CarRepairAppointment> repairAppointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carService" }, allowSetters = true)
    private Set<CarServiceEmployee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CarService name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public CarService address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CarRepairAppointment> getRepairAppointments() {
        return this.repairAppointments;
    }

    public void setRepairAppointments(Set<CarRepairAppointment> carRepairAppointments) {
        if (this.repairAppointments != null) {
            this.repairAppointments.forEach(i -> i.setCarService(null));
        }
        if (carRepairAppointments != null) {
            carRepairAppointments.forEach(i -> i.setCarService(this));
        }
        this.repairAppointments = carRepairAppointments;
    }

    public CarService repairAppointments(Set<CarRepairAppointment> carRepairAppointments) {
        this.setRepairAppointments(carRepairAppointments);
        return this;
    }

    public CarService addRepairAppointments(CarRepairAppointment carRepairAppointment) {
        this.repairAppointments.add(carRepairAppointment);
        carRepairAppointment.setCarService(this);
        return this;
    }

    public CarService removeRepairAppointments(CarRepairAppointment carRepairAppointment) {
        this.repairAppointments.remove(carRepairAppointment);
        carRepairAppointment.setCarService(null);
        return this;
    }

    public Set<CarServiceEmployee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<CarServiceEmployee> carServiceEmployees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setCarService(null));
        }
        if (carServiceEmployees != null) {
            carServiceEmployees.forEach(i -> i.setCarService(this));
        }
        this.employees = carServiceEmployees;
    }

    public CarService employees(Set<CarServiceEmployee> carServiceEmployees) {
        this.setEmployees(carServiceEmployees);
        return this;
    }

    public CarService addEmployees(CarServiceEmployee carServiceEmployee) {
        this.employees.add(carServiceEmployee);
        carServiceEmployee.setCarService(this);
        return this;
    }

    public CarService removeEmployees(CarServiceEmployee carServiceEmployee) {
        this.employees.remove(carServiceEmployee);
        carServiceEmployee.setCarService(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarService)) {
            return false;
        }
        return getId() != null && getId().equals(((CarService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarService{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
