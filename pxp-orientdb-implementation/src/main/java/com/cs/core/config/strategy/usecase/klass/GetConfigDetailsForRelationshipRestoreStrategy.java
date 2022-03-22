package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForRelationshipRestoreStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForRelationshipRestoreStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForRelationshipRestoreStrategy {
  
  @Override
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel execute(
      IGetConfigDetailsForSaveRelationshipInstancesRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_RELATIONSHIP_RESTORE, model,
        GetConfigDetailsForSaveRelationshipInstancesResponseModel.class);
  }
}
