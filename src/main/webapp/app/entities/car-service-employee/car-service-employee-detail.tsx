import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-service-employee.reducer';

export const CarServiceEmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carServiceEmployeeEntity = useAppSelector(state => state.carServiceEmployee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carServiceEmployeeDetailsHeading">
          <Translate contentKey="carRepairServiceApp.carServiceEmployee.detail.title">CarServiceEmployee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{carServiceEmployeeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="carRepairServiceApp.carServiceEmployee.name">Name</Translate>
            </span>
          </dt>
          <dd>{carServiceEmployeeEntity.name}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="carRepairServiceApp.carServiceEmployee.age">Age</Translate>
            </span>
          </dt>
          <dd>{carServiceEmployeeEntity.age}</dd>
          <dt>
            <span id="yearsOfExperience">
              <Translate contentKey="carRepairServiceApp.carServiceEmployee.yearsOfExperience">Years Of Experience</Translate>
            </span>
          </dt>
          <dd>{carServiceEmployeeEntity.yearsOfExperience}</dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.carServiceEmployee.carService">Car Service</Translate>
          </dt>
          <dd>{carServiceEmployeeEntity.carService ? carServiceEmployeeEntity.carService.address : ''}</dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.carServiceEmployee.repairAppointments">Repair Appointments</Translate>
          </dt>
          <dd>
            {carServiceEmployeeEntity.repairAppointments
              ? carServiceEmployeeEntity.repairAppointments.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {carServiceEmployeeEntity.repairAppointments && i === carServiceEmployeeEntity.repairAppointments.length - 1
                      ? ''
                      : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/car-service-employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car-service-employee/${carServiceEmployeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CarServiceEmployeeDetail;
