package com.cs.core.config.strategy.usecase.relationship;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.GetAllRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetAllRelationshipsStrategy extends OrientDBBaseStrategy
    implements IGetAllRelationshipsStrategy {
  
  @Override
  public IGetAllRelationshipsResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_ALL_RELATIONSHIPS, model, GetAllRelationshipsResponseModel.class);
  }
}
