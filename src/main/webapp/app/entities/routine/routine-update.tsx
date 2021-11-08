import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './routine.reducer';
import { IRoutine } from 'app/shared/model/routine.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RoutineUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const routineEntity = useAppSelector(state => state.routine.entity);
  const loading = useAppSelector(state => state.routine.loading);
  const updating = useAppSelector(state => state.routine.updating);
  const updateSuccess = useAppSelector(state => state.routine.updateSuccess);
  const handleClose = () => {
    props.history.push('/routine');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...routineEntity,
      ...values,
      users: mapIdList(values.users),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...routineEntity,
          users: routineEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterhelloreactApp.routine.home.createOrEditLabel" data-cy="RoutineCreateUpdateHeading">
            Create or edit a Routine
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="routine-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="routine-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Date Started" id="routine-dateStarted" name="dateStarted" data-cy="dateStarted" type="date" />
              <ValidatedField label="Date Ended" id="routine-dateEnded" name="dateEnded" data-cy="dateEnded" type="date" />
              <ValidatedField label="Goal Date" id="routine-goalDate" name="goalDate" data-cy="goalDate" type="date" />
              <ValidatedField
                label="Starting Body Weight"
                id="routine-startingBodyWeight"
                name="startingBodyWeight"
                data-cy="startingBodyWeight"
                type="text"
              />
              <ValidatedField
                label="Ending Body Weight"
                id="routine-endingBodyWeight"
                name="endingBodyWeight"
                data-cy="endingBodyWeight"
                type="text"
              />
              <ValidatedField label="User" id="routine-user" data-cy="user" type="select" multiple name="users">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/routine" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RoutineUpdate;
