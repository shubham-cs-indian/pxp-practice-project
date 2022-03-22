package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;

public interface ICreateRelationshipService extends ICreateConfigService<ICreateRelationshipModel, IGetRelationshipModel> {
  
}
