package com.carrepairservice.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.carrepairservice.app.domain.Car} entity. This class is used
 * in {@link com.carrepairservice.app.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter company;

    private IntegerFilter manufacturedYear;

    private StringFilter ownerName;

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.company = other.company == null ? null : other.company.copy();
        this.manufacturedYear = other.manufacturedYear == null ? null : other.manufacturedYear.copy();
        this.ownerName = other.ownerName == null ? null : other.ownerName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
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

    public StringFilter getCompany() {
        return company;
    }

    public StringFilter company() {
        if (company == null) {
            company = new StringFilter();
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public IntegerFilter getManufacturedYear() {
        return manufacturedYear;
    }

    public IntegerFilter manufacturedYear() {
        if (manufacturedYear == null) {
            manufacturedYear = new IntegerFilter();
        }
        return manufacturedYear;
    }

    public void setManufacturedYear(IntegerFilter manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public StringFilter getOwnerName() {
        return ownerName;
    }

    public StringFilter ownerName() {
        if (ownerName == null) {
            ownerName = new StringFilter();
        }
        return ownerName;
    }

    public void setOwnerName(StringFilter ownerName) {
        this.ownerName = ownerName;
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
        final CarCriteria that = (CarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(company, that.company) &&
            Objects.equals(manufacturedYear, that.manufacturedYear) &&
            Objects.equals(ownerName, that.ownerName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company, manufacturedYear, ownerName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (company != null ? "company=" + company + ", " : "") +
            (manufacturedYear != null ? "manufacturedYear=" + manufacturedYear + ", " : "") +
            (ownerName != null ? "ownerName=" + ownerName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
