import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="storeApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="storeApp.product.title">Title</Translate>
            </span>
          </dt>
          <dd>{productEntity.title}</dd>
          <dt>
            <span id="keywords">
              <Translate contentKey="storeApp.product.keywords">Keywords</Translate>
            </span>
          </dt>
          <dd>{productEntity.keywords}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="storeApp.product.description">Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="storeApp.product.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{productEntity.rating}</dd>
          <dt>
            <span id="dateAdded">
              <Translate contentKey="storeApp.product.dateAdded">Date Added</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.dateAdded ? <TextFormat value={productEntity.dateAdded} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateModified">
              <Translate contentKey="storeApp.product.dateModified">Date Modified</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.dateModified ? (
              <TextFormat value={productEntity.dateModified} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="storeApp.product.wishList">Wish List</Translate>
          </dt>
          <dd>{productEntity.wishList ? productEntity.wishList.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
