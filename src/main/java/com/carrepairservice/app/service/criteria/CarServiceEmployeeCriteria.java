package com.carrepairservice.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.carrepairservice.app.domain.CarServiceEmployee} entity. This class is used
 * in {@link com.carrepairservice.app.web.rest.CarServiceEmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /car-service-employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarServiceEmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter age;

    private IntegerFilter yearsOfExperience;

    private Boolean distinct;

    public CarServiceEmployeeCriteria() {}

    public CarServiceEmployeeCriteria(CarServiceEmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.yearsOfExperience = other.yearsOfExperience == null ? null : other.yearsOfExperience.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarServiceEmployeeCriteria copy() {
        return new CarServiceEmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public IntegerFilter age() {
        if (age == null) {
            age = new IntegerFilter();
        }
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public IntegerFilter getYearsOfExperience() {
        return yearsOfExperience;
    }

    public IntegerFilter yearsOfExperience() {
        if (yearsOfExperience == null) {
            yearsOfExperience = new IntegerFilter();
        }
        return yearsOfExperience;
    }

    public void setYearsOfExperience(IntegerFilter yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CarServiceEmployeeCriteria that = (CarServiceEmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(age, that.age) &&
            Objects.equals(yearsOfExperience, that.yearsOfExperience) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, yearsOfExperience, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarServiceEmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (age != null ? "age=" + age + ", " : "") +
            (yearsOfExperience != null ? "yearsOfExperience=" + yearsOfExperience + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
