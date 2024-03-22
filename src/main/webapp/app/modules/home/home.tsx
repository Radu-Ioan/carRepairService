import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="9">
        {account?.login ? (
          <div>
            <Alert color="success">Welcome, {account.login}.</Alert>
            <div className="container">
              If you want to make an appointment for your car, first register your car, then create an appointment.
            </div>
          </div>
        ) : (
          <div className="container d-flex gap-2 justify-content-center">
            <Button color="primary" size="lg" block>
              <Link to="/login" className="no-underline">
                Sign in
              </Link>
            </Button>
            <Button color="primary" size="lg" block>
              <Link to="/account/register" className="no-underline">
                Register
              </Link>
            </Button>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
