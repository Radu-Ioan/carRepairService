package com.carrepairservice.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.carrepairservice.app.domain.CarService} entity. This class is used
 * in {@link com.carrepairservice.app.web.rest.CarServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /car-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarServiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private LongFilter repairAppointmentsId;

    private LongFilter employeesId;

    private Boolean distinct;

    public CarServiceCriteria() {}

    public CarServiceCriteria(CarServiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.repairAppointmentsId = other.repairAppointmentsId == null ? null : other.repairAppointmentsId.copy();
        this.employeesId = other.employeesId == null ? null : other.employeesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarServiceCriteria copy() {
        return new CarServiceCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getRepairAppointmentsId() {
        return repairAppointmentsId;
    }

    public LongFilter repairAppointmentsId() {
        if (repairAppointmentsId == null) {
            repairAppointmentsId = new LongFilter();
        }
        return repairAppointmentsId;
    }

    public void setRepairAppointmentsId(LongFilter repairAppointmentsId) {
        this.repairAppointmentsId = repairAppointmentsId;
    }

    public LongFilter getEmployeesId() {
        return employeesId;
    }

    public LongFilter employeesId() {
        if (employeesId == null) {
            employeesId = new LongFilter();
        }
        return employeesId;
    }

    public void setEmployeesId(LongFilter employeesId) {
        this.employeesId = employeesId;
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
        final CarServiceCriteria that = (CarServiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(repairAppointmentsId, that.repairAppointmentsId) &&
            Objects.equals(employeesId, that.employeesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, repairAppointmentsId, employeesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarServiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (repairAppointmentsId != null ? "repairAppointmentsId=" + repairAppointmentsId + ", " : "") +
            (employeesId != null ? "employeesId=" + employeesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
