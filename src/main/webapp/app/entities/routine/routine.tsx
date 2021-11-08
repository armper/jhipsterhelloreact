import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './routine.reducer';
import { IRoutine } from 'app/shared/model/routine.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Routine = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const routineList = useAppSelector(state => state.routine.entities);
  const loading = useAppSelector(state => state.routine.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="routine-heading" data-cy="RoutineHeading">
        Routines
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Routine
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {routineList && routineList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Date Started</th>
                <th>Date Ended</th>
                <th>Goal Date</th>
                <th>Starting Body Weight</th>
                <th>Ending Body Weight</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {routineList.map((routine, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${routine.id}`} color="link" size="sm">
                      {routine.id}
                    </Button>
                  </td>
                  <td>{routine.name}</td>
                  <td>
                    {routine.dateStarted ? <TextFormat type="date" value={routine.dateStarted} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{routine.dateEnded ? <TextFormat type="date" value={routine.dateEnded} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{routine.goalDate ? <TextFormat type="date" value={routine.goalDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{routine.startingBodyWeight}</td>
                  <td>{routine.endingBodyWeight}</td>
                  <td>
                    {routine.users
                      ? routine.users.map((val, j) => (
                          <span key={j}>
                            {val.id}
                            {j === routine.users.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${routine.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${routine.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${routine.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Routines found</div>
        )}
      </div>
    </div>
  );
};

export default Routine;
