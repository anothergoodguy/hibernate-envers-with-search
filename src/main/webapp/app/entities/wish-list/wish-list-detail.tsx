import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wish-list.reducer';

export const WishListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wishListEntity = useAppSelector(state => state.wishList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wishListDetailsHeading">
          <Translate contentKey="storeApp.wishList.detail.title">WishList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wishListEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="storeApp.wishList.title">Title</Translate>
            </span>
          </dt>
          <dd>{wishListEntity.title}</dd>
          <dt>
            <span id="restricted">
              <Translate contentKey="storeApp.wishList.restricted">Restricted</Translate>
            </span>
          </dt>
          <dd>{wishListEntity.restricted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="storeApp.wishList.customer">Customer</Translate>
          </dt>
          <dd>{wishListEntity.customer ? wishListEntity.customer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/wish-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wish-list/${wishListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WishListDetail;
