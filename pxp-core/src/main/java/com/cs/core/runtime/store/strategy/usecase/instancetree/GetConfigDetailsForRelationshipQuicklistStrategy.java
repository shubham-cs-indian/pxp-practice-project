package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRelationshipQuicklistResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;

@Component
public class GetConfigDetailsForRelationshipQuicklistStrategy extends OrientDBBaseStrategy 
  implements IGetConfigDetailsForRelationshipQuicklistStrategy {

  @Override
  public IConfigDetailsForRelationshipQuicklistResponseModel execute(
      IConfigDetailsForRelationshipQuicklistRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAIL_FOR_RELATIONSHIP_QUICKLIST, model, ConfigDetailsForRelationshipQuicklistResponseModel.class);
  }
  
}
