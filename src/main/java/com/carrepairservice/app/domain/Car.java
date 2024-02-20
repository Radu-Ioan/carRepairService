package com.carrepairservice.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "company")
    private String company;

    @Min(value = 1970)
    @Column(name = "manufactured_year")
    private Integer manufacturedYear;

    @NotNull
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @JsonIgnoreProperties(value = { "car", "carService" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CarRepairAppointment carRepairAppointment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return this.company;
    }

    public Car company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getManufacturedYear() {
        return this.manufacturedYear;
    }

    public Car manufacturedYear(Integer manufacturedYear) {
        this.setManufacturedYear(manufacturedYear);
        return this;
    }

    public void setManufacturedYear(Integer manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Car ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public CarRepairAppointment getCarRepairAppointment() {
        return this.carRepairAppointment;
    }

    public void setCarRepairAppointment(CarRepairAppointment carRepairAppointment) {
        this.carRepairAppointment = carRepairAppointment;
    }

    public Car carRepairAppointment(CarRepairAppointment carRepairAppointment) {
        this.setCarRepairAppointment(carRepairAppointment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return getId() != null && getId().equals(((Car) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", manufacturedYear=" + getManufacturedYear() +
            ", ownerName='" + getOwnerName() + "'" +
            "}";
    }
}
