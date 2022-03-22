package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;

public interface ISaveRelationshipService extends ISaveConfigService<ISaveRelationshipModel, IGetRelationshipModel> {
  
}
