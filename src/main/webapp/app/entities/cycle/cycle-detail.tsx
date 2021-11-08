import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './cycle.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CycleDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cycleEntity = useAppSelector(state => state.cycle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cycleDetailsHeading">Cycle</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cycleEntity.id}</dd>
          <dt>
            <span id="reps">Reps</span>
          </dt>
          <dd>{cycleEntity.reps}</dd>
          <dt>
            <span id="volume">Volume</span>
          </dt>
          <dd>{cycleEntity.volume}</dd>
          <dt>Excercise</dt>
          <dd>{cycleEntity.excercise ? cycleEntity.excercise.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cycle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cycle/${cycleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CycleDetail;
