import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICarService } from 'app/shared/model/car-service.model';
import { getEntities as getCarServices } from 'app/entities/car-service/car-service.reducer';
import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';
import { getEntities as getCarRepairAppointments } from 'app/entities/car-repair-appointment/car-repair-appointment.reducer';
import { ICarServiceEmployee } from 'app/shared/model/car-service-employee.model';
import { getEntity, updateEntity, createEntity, reset } from './car-service-employee.reducer';

export const CarServiceEmployeeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const carServices = useAppSelector(state => state.carService.entities);
  let carRepairAppointments = useAppSelector(state => state.carRepairAppointment.entities);

  const carServiceEmployeeEntity = useAppSelector(state => state.carServiceEmployee.entity);
  carRepairAppointments = carRepairAppointments.filter(
    (app: ICarRepairAppointment) => app.carService?.address == carServiceEmployeeEntity.carService?.address,
  );

  const loading = useAppSelector(state => state.carServiceEmployee.loading);
  const updating = useAppSelector(state => state.carServiceEmployee.updating);
  const updateSuccess = useAppSelector(state => state.carServiceEmployee.updateSuccess);

  const handleClose = () => {
    navigate('/car-service-employee' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCarServices({}));
    dispatch(getCarRepairAppointments({}));
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
    if (values.age !== undefined && typeof values.age !== 'number') {
      values.age = Number(values.age);
    }
    if (values.yearsOfExperience !== undefined && typeof values.yearsOfExperience !== 'number') {
      values.yearsOfExperience = Number(values.yearsOfExperience);
    }

    const entity = {
      ...carServiceEmployeeEntity,
      ...values,
      repairAppointments: mapIdList(values.repairAppointments),
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
          ...carServiceEmployeeEntity,
          carService: carServiceEmployeeEntity?.carService?.id,
          repairAppointments: carServiceEmployeeEntity?.repairAppointments?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="carRepairServiceApp.carServiceEmployee.home.createOrEditLabel" data-cy="CarServiceEmployeeCreateUpdateHeading">
            <Translate contentKey="carRepairServiceApp.carServiceEmployee.home.createOrEditLabel">
              Create or edit a CarServiceEmployee
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
                  id="car-service-employee-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('carRepairServiceApp.carServiceEmployee.name')}
                id="car-service-employee-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('carRepairServiceApp.carServiceEmployee.age')}
                id="car-service-employee-age"
                name="age"
                data-cy="age"
                type="text"
                validate={{
                  min: { value: 2, message: translate('entity.validation.min', { min: 2 }) },
                  max: { value: 140, message: translate('entity.validation.max', { max: 140 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('carRepairServiceApp.carServiceEmployee.yearsOfExperience')}
                id="car-service-employee-yearsOfExperience"
                name="yearsOfExperience"
                data-cy="yearsOfExperience"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="car-service-employee-carService"
                name="carService"
                data-cy="carService"
                label={translate('carRepairServiceApp.carServiceEmployee.carService')}
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
              <ValidatedField
                label={translate('carRepairServiceApp.carServiceEmployee.repairAppointments')}
                id="car-service-employee-repairAppointments"
                data-cy="repairAppointments"
                type="select"
                multiple
                name="repairAppointments"
              >
                <option value="" key="0" />
                {carRepairAppointments
                  ? carRepairAppointments.map(appointment => {
                      return (
                        <option value={appointment.id} key={appointment.id}>
                          {appointment.id}
                        </option>
                      );
                    })
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/car-service-employee" replace color="info">
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

export default CarServiceEmployeeUpdate;
