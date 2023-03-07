import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryStatus } from 'app/shared/model/enumerations/category-status.model';
import { getEntity, updateEntity, createEntity, reset } from './category.reducer';

export const CategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categories = useAppSelector(state => state.category.entities);
  const products = useAppSelector(state => state.product.entities);
  const categoryEntity = useAppSelector(state => state.category.entity);
  const loading = useAppSelector(state => state.category.loading);
  const updating = useAppSelector(state => state.category.updating);
  const updateSuccess = useAppSelector(state => state.category.updateSuccess);
  const categoryStatusValues = Object.keys(CategoryStatus);

  const handleClose = () => {
    navigate('/category');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCategories({}));
    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...categoryEntity,
      ...values,
      products: mapIdList(values.products),
      parent: categories.find(it => it.id.toString() === values.parent.toString()),
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
          status: 'AVAILABLE',
          ...categoryEntity,
          parent: categoryEntity?.parent?.id,
          products: categoryEntity?.products?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="storeApp.category.home.createOrEditLabel" data-cy="CategoryCreateUpdateHeading">
            <Translate contentKey="storeApp.category.home.createOrEditLabel">Create or edit a Category</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="category-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('storeApp.category.description')}
                id="category-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('storeApp.category.sortOrder')}
                id="category-sortOrder"
                name="sortOrder"
                data-cy="sortOrder"
                type="text"
              />
              <ValidatedField
                label={translate('storeApp.category.dateAdded')}
                id="category-dateAdded"
                name="dateAdded"
                data-cy="dateAdded"
                type="date"
              />
              <ValidatedField
                label={translate('storeApp.category.dateModified')}
                id="category-dateModified"
                name="dateModified"
                data-cy="dateModified"
                type="date"
              />
              <ValidatedField
                label={translate('storeApp.category.status')}
                id="category-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {categoryStatusValues.map(categoryStatus => (
                  <option value={categoryStatus} key={categoryStatus}>
                    {translate('storeApp.CategoryStatus.' + categoryStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="category-parent"
                name="parent"
                data-cy="parent"
                label={translate('storeApp.category.parent')}
                type="select"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('storeApp.category.product')}
                id="category-product"
                data-cy="product"
                type="select"
                multiple
                name="products"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/category" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CategoryUpdate;
