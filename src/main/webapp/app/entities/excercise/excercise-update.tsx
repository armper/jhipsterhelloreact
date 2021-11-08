import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRoutine } from 'app/shared/model/routine.model';
import { getEntities as getRoutines } from 'app/entities/routine/routine.reducer';
import { getEntity, updateEntity, createEntity, reset } from './excercise.reducer';
import { IExcercise } from 'app/shared/model/excercise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ExcerciseType } from 'app/shared/model/enumerations/excercise-type.model';

export const ExcerciseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const routines = useAppSelector(state => state.routine.entities);
  const excerciseEntity = useAppSelector(state => state.excercise.entity);
  const loading = useAppSelector(state => state.excercise.loading);
  const updating = useAppSelector(state => state.excercise.updating);
  const updateSuccess = useAppSelector(state => state.excercise.updateSuccess);
  const excerciseTypeValues = Object.keys(ExcerciseType);
  const handleClose = () => {
    props.history.push('/excercise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRoutines({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...excerciseEntity,
      ...values,
      routine: routines.find(it => it.id.toString() === values.routine.toString()),
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
          type: 'BARBELL',
          ...excerciseEntity,
          routine: excerciseEntity?.routine?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterhelloreactApp.excercise.home.createOrEditLabel" data-cy="ExcerciseCreateUpdateHeading">
            Create or edit a Excercise
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="excercise-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type" id="excercise-type" name="type" data-cy="type" type="select">
                {excerciseTypeValues.map(excerciseType => (
                  <option value={excerciseType} key={excerciseType}>
                    {excerciseType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Current Volume"
                id="excercise-currentVolume"
                name="currentVolume"
                data-cy="currentVolume"
                type="text"
              />
              <ValidatedField
                label="Starting Volume"
                id="excercise-startingVolume"
                name="startingVolume"
                data-cy="startingVolume"
                type="text"
              />
              <ValidatedField label="Goal Volume" id="excercise-goalVolume" name="goalVolume" data-cy="goalVolume" type="text" />
              <ValidatedField id="excercise-routine" name="routine" data-cy="routine" label="Routine" type="select">
                <option value="" key="0" />
                {routines
                  ? routines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/excercise" replace color="info">
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

export default ExcerciseUpdate;
