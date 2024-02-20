import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICar } from 'app/shared/model/car.model';
import { getEntities as getCars } from 'app/entities/car/car.reducer';
import { ICarService } from 'app/shared/model/car-service.model';
import { getEntities as getCarServices } from 'app/entities/car-service/car-service.reducer';
import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';
import { getEntity, updateEntity, createEntity, reset } from './car-repair-appointment.reducer';

export const CarRepairAppointmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cars = useAppSelector(state => state.car.entities);
  const carServices = useAppSelector(state => state.carService.entities);
  const carRepairAppointmentEntity = useAppSelector(state => state.carRepairAppointment.entity);
  const loading = useAppSelector(state => state.carRepairAppointment.loading);
  const updating = useAppSelector(state => state.carRepairAppointment.updating);
  const updateSuccess = useAppSelector(state => state.carRepairAppointment.updateSuccess);

  const handleClose = () => {
    navigate('/car-repair-appointment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCars({}));
    dispatch(getCarServices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...carRepairAppointmentEntity,
      ...values,
      car: cars.find(it => it.id.toString() === values.car.toString()),
      carService: carServices.find(it => it.id.toString() === values.carService.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...carRepairAppointmentEntity,
          car: carRepairAppointmentEntity?.car?.id,
          carService: carRepairAppointmentEntity?.carService?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="carRepairServiceApp.carRepairAppointment.home.createOrEditLabel" data-cy="CarRepairAppointmentCreateUpdateHeading">
            <Translate contentKey="carRepairServiceApp.carRepairAppointment.home.createOrEditLabel">
              Create or edit a CarRepairAppointment
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="car-repair-appointment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('carRepairServiceApp.carRepairAppointment.date')}
                id="car-repair-appointment-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="car-repair-appointment-car"
                name="car"
                data-cy="car"
                label={translate('carRepairServiceApp.carRepairAppointment.car')}
                type="select"
                required
              >
                <option value="" key="0" />
                {cars
                  ? cars.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.ownerName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="car-repair-appointment-carService"
                name="carService"
                data-cy="carService"
                label={translate('carRepairServiceApp.carRepairAppointment.carService')}
                type="select"
                required
              >
                <option value="" key="0" />
                {carServices
                  ? carServices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.address}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/car-repair-appointment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CarRepairAppointmentUpdate;
