import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-service.reducer';

export const CarServiceDetail = () => {
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
        </dl>
        <Button tag={Link} to="/car-service" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car-service/${carServiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CarServiceDetail;
