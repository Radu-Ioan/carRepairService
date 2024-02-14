import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-repair-appointment.reducer';

export const CarRepairAppointmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carRepairAppointmentEntity = useAppSelector(state => state.carRepairAppointment.entity);
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
        </dl>
        <Button tag={Link} to="/car-repair-appointment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car-repair-appointment/${carRepairAppointmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CarRepairAppointmentDetail;
