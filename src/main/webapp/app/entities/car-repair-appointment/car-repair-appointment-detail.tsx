import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-repair-appointment.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const CarRepairAppointmentDetail = () => {
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carRepairAppointmentEntity = useAppSelector(state => state.carRepairAppointment.entity);

  console.log('carRepairAppointmentEntity.car:', carRepairAppointmentEntity.car);
  const isOwner = `${account.lastName} ${account.firstName}` == carRepairAppointmentEntity.car?.ownerName.trim();

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carRepairAppointmentDetailsHeading">
          <Translate contentKey="carRepairServiceApp.carRepairAppointment.detail.title">CarRepairAppointment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{carRepairAppointmentEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="carRepairServiceApp.carRepairAppointment.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {carRepairAppointmentEntity.date ? (
              <TextFormat value={carRepairAppointmentEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.carRepairAppointment.car">Car</Translate>
          </dt>
          <dd>{carRepairAppointmentEntity.car ? carRepairAppointmentEntity.car.ownerName : ''}</dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.carRepairAppointment.carService">Car Service</Translate>
          </dt>
          <dd>{carRepairAppointmentEntity.carService ? carRepairAppointmentEntity.carService.address : ''}</dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.carRepairAppointment.responsibleEmployees">Responsible Employees</Translate>
          </dt>
          <dd>
            {carRepairAppointmentEntity.responsibleEmployees
              ? carRepairAppointmentEntity.responsibleEmployees.map((val, i) => (
                  <span key={val.id}>
                    <Link to={'/car-service-employee/' + val.id} className="no-underline">
                      {val.name}
                    </Link>
                    {carRepairAppointmentEntity.responsibleEmployees && i === carRepairAppointmentEntity.responsibleEmployees.length - 1
                      ? ''
                      : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/car-repair-appointment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">All appointments</span>
        </Button>
        &nbsp;
        {isOwner && (
          <Button tag={Link} to={`/car-repair-appointment/${carRepairAppointmentEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        )}
      </Col>
    </Row>
  );
};

export default CarRepairAppointmentDetail;
