package com.carrepairservice.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.carrepairservice.app.domain.CarRepairAppointment} entity. This class is used
 * in {@link com.carrepairservice.app.web.rest.CarRepairAppointmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /car-repair-appointments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarRepairAppointmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private LongFilter carId;

    private LongFilter carServiceId;

    private LongFilter responsibleEmployeesId;

    private Boolean distinct;

    public CarRepairAppointmentCriteria() {}

    public CarRepairAppointmentCriteria(CarRepairAppointmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.carId = other.carId == null ? null : other.carId.copy();
        this.carServiceId = other.carServiceId == null ? null : other.carServiceId.copy();
        this.responsibleEmployeesId = other.responsibleEmployeesId == null ? null : other.responsibleEmployeesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarRepairAppointmentCriteria copy() {
        return new CarRepairAppointmentCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getCarId() {
        return carId;
    }

    public LongFilter carId() {
        if (carId == null) {
            carId = new LongFilter();
        }
        return carId;
    }

    public void setCarId(LongFilter carId) {
        this.carId = carId;
    }

    public LongFilter getCarServiceId() {
        return carServiceId;
    }

    public LongFilter carServiceId() {
        if (carServiceId == null) {
            carServiceId = new LongFilter();
        }
        return carServiceId;
    }

    public void setCarServiceId(LongFilter carServiceId) {
        this.carServiceId = carServiceId;
    }

    public LongFilter getResponsibleEmployeesId() {
        return responsibleEmployeesId;
    }

    public LongFilter responsibleEmployeesId() {
        if (responsibleEmployeesId == null) {
            responsibleEmployeesId = new LongFilter();
        }
        return responsibleEmployeesId;
    }

    public void setResponsibleEmployeesId(LongFilter responsibleEmployeesId) {
        this.responsibleEmployeesId = responsibleEmployeesId;
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
        final CarRepairAppointmentCriteria that = (CarRepairAppointmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(carServiceId, that.carServiceId) &&
            Objects.equals(responsibleEmployeesId, that.responsibleEmployeesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, carId, carServiceId, responsibleEmployeesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarRepairAppointmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (carId != null ? "carId=" + carId + ", " : "") +
            (carServiceId != null ? "carServiceId=" + carServiceId + ", " : "") +
            (responsibleEmployeesId != null ? "responsibleEmployeesId=" + responsibleEmployeesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
