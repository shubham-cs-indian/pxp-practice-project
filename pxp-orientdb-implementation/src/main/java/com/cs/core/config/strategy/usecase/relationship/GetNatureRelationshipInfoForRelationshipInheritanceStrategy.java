package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.entity.relationshipinstance.GetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;

@Component
public class GetNatureRelationshipInfoForRelationshipInheritanceStrategy extends OrientDBBaseStrategy
    implements IGetNatureRelationshipInfoForRelationshipInheritanceStrategy {
  
  @Override
  public IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel execute(
      IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel model) throws Exception
  {
    return execute(GET_NATURE_RELATIONSHIP_INFO, model, GetNatureRelationshipInfoForRelationshipInheritanceResponseModel.class);
  }
}
