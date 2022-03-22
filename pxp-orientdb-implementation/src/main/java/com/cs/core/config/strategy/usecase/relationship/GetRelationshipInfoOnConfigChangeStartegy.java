package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.entity.relationshipinstance.GetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IConfigChangeRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;

@Component
public class GetRelationshipInfoOnConfigChangeStartegy extends OrientDBBaseStrategy
    implements IGetRelationshipInfoOnConfigChangeStartegy {
  
  @Override
  public IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel execute(
      IConfigChangeRelationshipInheritanceModel model) throws Exception
  {
    return execute(GET_RELATIONSHIP_INFO_ON_CONFIG_CHANGE, model,
        GetNatureRelationshipInfoForRelationshipInheritanceResponseModel.class);
  }
  
}
