import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="9">
        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div className="container d-flex gap-1 justify-content-center">
            <Link to="/login" className="no-underline">
              Sign in
            </Link>
            <Link to="/account/register" className="no-underline">
              Register
            </Link>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
