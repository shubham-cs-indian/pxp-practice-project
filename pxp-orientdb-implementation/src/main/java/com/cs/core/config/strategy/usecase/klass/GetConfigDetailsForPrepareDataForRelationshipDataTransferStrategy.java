package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForPrepareDataForRelationshipDataTransferStrategy;

@Component
public class GetConfigDetailsForPrepareDataForRelationshipDataTransferStrategy
    extends OrientDBBaseStrategy
    implements IGetConfigDetailsForPrepareDataForRelationshipDataTransferStrategy {
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_PREPARE_DATA_FOR_RELATIONSHIP_DATA_TRANSFER, model,
        GetConfigDetailsForCustomTabModel.class);
  }
}
