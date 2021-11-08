import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './excercise.reducer';
import { IExcercise } from 'app/shared/model/excercise.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Excercise = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const excerciseList = useAppSelector(state => state.excercise.entities);
  const loading = useAppSelector(state => state.excercise.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="excercise-heading" data-cy="ExcerciseHeading">
        Excercises
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Excercise
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {excerciseList && excerciseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Current Volume</th>
                <th>Starting Volume</th>
                <th>Goal Volume</th>
                <th>Routine</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {excerciseList.map((excercise, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${excercise.id}`} color="link" size="sm">
                      {excercise.id}
                    </Button>
                  </td>
                  <td>{excercise.type}</td>
                  <td>{excercise.currentVolume}</td>
                  <td>{excercise.startingVolume}</td>
                  <td>{excercise.goalVolume}</td>
                  <td>{excercise.routine ? <Link to={`routine/${excercise.routine.id}`}>{excercise.routine.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${excercise.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${excercise.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${excercise.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Excercises found</div>
        )}
      </div>
    </div>
  );
};

export default Excercise;
