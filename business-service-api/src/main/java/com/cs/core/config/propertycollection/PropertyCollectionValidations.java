package com.cs.core.config.propertycollection;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.exception.InvalidCodeException;
import com.cs.core.exception.LabelMustNotBeEmptyException;

public class PropertyCollectionValidations extends Validations {

  public static void validateCreatePropertyCollection(IPropertyCollectionModel model) throws InvalidCodeException, LabelMustNotBeEmptyException
  {
    validateCode(model.getCode());
    validateLabel(model.getLabel());
  }

  public static void validateSavePropertyCollection(ISavePropertyCollectionModel model) throws InvalidCodeException, LabelMustNotBeEmptyException
  {
    validateCode(model.getCode());
    validateLabel(model.getLabel());
  }

}
