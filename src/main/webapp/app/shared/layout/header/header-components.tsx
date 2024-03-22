import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <span className="brand-title">Supercricul</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Employee = () => (
  <NavItem>
    <NavLink tag={Link} to="/car-service-employee" className="d-flex align-items center">
      <span>Services workers</span>
    </NavLink>
  </NavItem>
);

export const Services = () => (
  <NavItem>
    <NavLink tag={Link} to="/car-service" className="d-flex align-items center">
      <span>Services</span>
    </NavLink>
  </NavItem>
);

export const Car = () => (
  <NavItem>
    <NavLink tag={Link} to="/car" className="d-flex align-items center">
      <span>Cars</span>
    </NavLink>
  </NavItem>
);

export const Appointment = () => (
  <NavItem>
    <NavLink tag={Link} to="/car-repair-appointment" className="d-flex align-items center">
      <span>Appointments</span>
    </NavLink>
  </NavItem>
);
