import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IExcercise } from 'app/shared/model/excercise.model';
import { getEntities as getExcercises } from 'app/entities/excercise/excercise.reducer';
import { getEntity, updateEntity, createEntity, reset } from './cycle.reducer';
import { ICycle } from 'app/shared/model/cycle.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CycleUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const excercises = useAppSelector(state => state.excercise.entities);
  const cycleEntity = useAppSelector(state => state.cycle.entity);
  const loading = useAppSelector(state => state.cycle.loading);
  const updating = useAppSelector(state => state.cycle.updating);
  const updateSuccess = useAppSelector(state => state.cycle.updateSuccess);
  const handleClose = () => {
    props.history.push('/cycle');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExcercises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cycleEntity,
      ...values,
      excercise: excercises.find(it => it.id.toString() === values.excercise.toString()),
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
          ...cycleEntity,
          excercise: cycleEntity?.excercise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterhelloreactApp.cycle.home.createOrEditLabel" data-cy="CycleCreateUpdateHeading">
            Create or edit a Cycle
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="cycle-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Reps" id="cycle-reps" name="reps" data-cy="reps" type="text" />
              <ValidatedField label="Volume" id="cycle-volume" name="volume" data-cy="volume" type="text" />
              <ValidatedField id="cycle-excercise" name="excercise" data-cy="excercise" label="Excercise" type="select">
                <option value="" key="0" />
                {excercises
                  ? excercises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cycle" replace color="info">
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

export default CycleUpdate;
