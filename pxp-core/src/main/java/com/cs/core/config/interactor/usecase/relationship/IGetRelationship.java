package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRelationship
    extends IGetConfigInteractor<IIdParameterModel, IGetRelationshipModel> {
  
}
