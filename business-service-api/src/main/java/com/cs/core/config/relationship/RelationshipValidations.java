package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;

public class RelationshipValidations extends Validations {

  public static void validateCreate(ICreateRelationshipModel dataModel) throws Exception
  {
    Validations.validateCode(dataModel.getCode());
    Validations.validateLabel(dataModel.getLabel());
  }


  public static void validateSave(ISaveRelationshipModel dataModel) throws Exception
  {
    Validations.validateLabel(dataModel.getLabel());
  }

}
