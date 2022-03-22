package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.entity.relationshipinstance.GetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy extends OrientDBBaseStrategy implements IGetRelationshipInheritanceInfoOnNatureRelationshipChangeStartegy{

  @Override
  public IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel execute(
      IIdsListParameterModel model) throws Exception
  {
    return execute(GET_NATURE_RELATIONSHIP_INFO_ON_NATURE_CHANGE, model,
          GetNatureRelationshipInfoForRelationshipInheritanceResponseModel.class);
    
  }
}
  