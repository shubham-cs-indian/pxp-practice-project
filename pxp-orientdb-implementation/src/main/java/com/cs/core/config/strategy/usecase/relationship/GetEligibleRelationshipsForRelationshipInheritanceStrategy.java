package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.references.IGetEligibleRelationshipsForRelationshipInheritanceStrategy;


@Component
public class GetEligibleRelationshipsForRelationshipInheritanceStrategy extends OrientDBBaseStrategy
    implements IGetEligibleRelationshipsForRelationshipInheritanceStrategy {

  @Override
  public IGetConfigDataEntityResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_ELIGIBLE_RELATIONSHIPS_FOR_RELATIONSHIP_INHERITANCE , model , GetConfigDataEntityResponseModel.class);
  }
  
}
