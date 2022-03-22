package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ISaveInstanceRelationshipsService extends IRuntimeService <ISaveRelationshipInstanceModel,IGetKlassInstanceModel> {
  
  public IKlassInstanceTypeModel executeSaveRelationship(ISaveRelationshipInstanceModel dataModel) throws Exception;
  
}
