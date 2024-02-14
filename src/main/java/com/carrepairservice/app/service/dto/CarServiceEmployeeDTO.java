package com.carrepairservice.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.carrepairservice.app.domain.CarServiceEmployee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarServiceEmployeeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Min(value = 2)
    @Max(value = 140)
    private Integer age;

    @Min(value = 0)
    private Integer yearsOfExperience;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarServiceEmployeeDTO)) {
            return false;
        }

        CarServiceEmployeeDTO carServiceEmployeeDTO = (CarServiceEmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carServiceEmployeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarServiceEmployeeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", yearsOfExperience=" + getYearsOfExperience() +
            "}";
    }
}
