package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;

public interface ICreateRelationship
    extends ICreateConfigInteractor<ICreateRelationshipModel, IGetRelationshipModel> {
  
}
