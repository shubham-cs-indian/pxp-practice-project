package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;

@Component
public class GetConfigDetailsForGetNewInstanceTreeStrategy extends OrientDBBaseStrategy 
  implements IGetConfigDetailsForGetNewInstanceTreeStrategy {

  @Override
  public IConfigDetailsForGetNewInstanceTreeModel execute(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_NEW_INSTANCE_TREE, model, ConfigDetailsForGetNewInstanceTreeModel.class);
  }
  
}
