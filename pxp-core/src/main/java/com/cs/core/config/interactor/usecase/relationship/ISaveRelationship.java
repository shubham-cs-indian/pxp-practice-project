package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;

public interface ISaveRelationship
    extends ISaveConfigInteractor<ISaveRelationshipModel, IGetRelationshipModel> {
  
}
