import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const CarDetail = () => {
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carEntity = useAppSelector(state => state.car.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carDetailsHeading">
          <Translate contentKey="carRepairServiceApp.car.detail.title">Car</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{carEntity.id}</dd>
          <dt>
            <span id="company">
              <Translate contentKey="carRepairServiceApp.car.company">Company</Translate>
            </span>
          </dt>
          <dd>{carEntity.company}</dd>
          <dt>
            <span id="manufacturedYear">
              <Translate contentKey="carRepairServiceApp.car.manufacturedYear">Manufactured Year</Translate>
            </span>
          </dt>
          <dd>{carEntity.manufacturedYear}</dd>
          <dt>
            <span id="ownerName">
              <Translate contentKey="carRepairServiceApp.car.ownerName">Owner Name</Translate>
            </span>
          </dt>
          <dd>{carEntity.ownerName}</dd>
          <dt>
            <Translate contentKey="carRepairServiceApp.car.carRepairAppointment">Car Repair Appointment</Translate>
          </dt>
          <dd>{carEntity.carRepairAppointment ? carEntity.carRepairAppointment.date : ''}</dd>
        </dl>
        <Button tag={Link} to="/car" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">All cars</span>
        </Button>
        &nbsp;
        {isAdmin && (
          <Button tag={Link} to={`/car/${carEntity.id}/edit`} replace color="primary">
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

export default CarDetail;
