package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.entity.relationshipinstance.ConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipInheritanceOnTypeSwitchRequestModel;

@Component
public class GetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy extends OrientDBBaseStrategy implements IGetConfigDetailsForRelationshipInheritanceOnTypeSwitchStrategy{

  @Override
  public IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel execute(
      IRelationshipInheritanceOnTypeSwitchRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_REL_INHERITANCE_ON_TYPE_SWITCH, model,
        ConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.class);
  }
  
}
