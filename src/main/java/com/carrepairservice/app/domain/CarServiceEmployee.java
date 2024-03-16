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
 * A CarServiceEmployee.
 */
@Entity
@Table(name = "car_service_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarServiceEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 2)
    @Max(value = 140)
    @Column(name = "age")
    private Integer age;

    @Min(value = 0)
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "repairAppointments", "employees" }, allowSetters = true)
    private CarService carService;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_car_service_employee__repair_appointments",
        joinColumns = @JoinColumn(name = "car_service_employee_id"),
        inverseJoinColumns = @JoinColumn(name = "repair_appointments_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car", "carService", "responsibleEmployees" }, allowSetters = true)
    private Set<CarRepairAppointment> repairAppointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarServiceEmployee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CarServiceEmployee name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public CarServiceEmployee age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getYearsOfExperience() {
        return this.yearsOfExperience;
    }

    public CarServiceEmployee yearsOfExperience(Integer yearsOfExperience) {
        this.setYearsOfExperience(yearsOfExperience);
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public CarService getCarService() {
        return this.carService;
    }

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    public CarServiceEmployee carService(CarService carService) {
        this.setCarService(carService);
        return this;
    }

    public Set<CarRepairAppointment> getRepairAppointments() {
        return this.repairAppointments;
    }

    public void setRepairAppointments(Set<CarRepairAppointment> carRepairAppointments) {
        this.repairAppointments = carRepairAppointments;
    }

    public CarServiceEmployee repairAppointments(Set<CarRepairAppointment> carRepairAppointments) {
        this.setRepairAppointments(carRepairAppointments);
        return this;
    }

    public CarServiceEmployee addRepairAppointments(CarRepairAppointment carRepairAppointment) {
        this.repairAppointments.add(carRepairAppointment);
        return this;
    }

    public CarServiceEmployee removeRepairAppointments(CarRepairAppointment carRepairAppointment) {
        this.repairAppointments.remove(carRepairAppointment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarServiceEmployee)) {
            return false;
        }
        return getId() != null && getId().equals(((CarServiceEmployee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarServiceEmployee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", yearsOfExperience=" + getYearsOfExperience() +
            "}";
    }
}
