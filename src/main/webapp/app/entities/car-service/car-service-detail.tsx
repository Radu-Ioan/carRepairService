import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-service.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const CarServiceDetail = () => {
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carServiceEntity = useAppSelector(state => state.carService.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carServiceDetailsHeading">
          <Translate contentKey="carRepairServiceApp.carService.detail.title">CarService</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{carServiceEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="carRepairServiceApp.carService.name">Name</Translate>
            </span>
          </dt>
          <dd>{carServiceEntity.name}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="carRepairServiceApp.carService.address">Address</Translate>
            </span>
          </dt>
          <dd>{carServiceEntity.address}</dd>
          <dt>
            <span>Repair appointments</span>
          </dt>
          <dd>
            {carServiceEntity.repairAppointments &&
              carServiceEntity.repairAppointments.map((app, idx) => {
                return (
                  <span key={app.id}>
                    <Link to={'/car-repair-appointment/' + app.id} className="no-underline">
                      {app.id}
                    </Link>
                    {carServiceEntity.repairAppointments && idx === carServiceEntity.repairAppointments.length - 1 ? '' : ', '}
                  </span>
                );
              })}
          </dd>
          <dt>
            <span>Employees</span>
          </dt>
          <dd>
            {carServiceEntity.employees &&
              carServiceEntity.employees.map((emp, idx) => {
                return (
                  <span key={emp.id}>
                    <Link to={'/car-service-employee/' + emp.id} className="no-underline">
                      {emp.name}
                    </Link>
                    {carServiceEntity.employees && idx === carServiceEntity.employees.length - 1 ? '' : ', '}
                  </span>
                );
              })}
          </dd>
        </dl>
        <Button tag={Link} to="/car-service" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">All car services</span>
        </Button>
        &nbsp;
        {isAdmin && (
          <Button tag={Link} to={`/car-service/${carServiceEntity.id}/edit`} replace color="primary">
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

export default CarServiceDetail;
