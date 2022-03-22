package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.strategy.usecase.tableview.IGetConfigDetailsForTableViewStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForTableViewStrategy extends OrientDBBaseStrategy
  implements IGetConfigDetailsForTableViewStrategy {

  @Override
  public IConfigDetailsForGetNewInstanceTreeModel execute(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_TABLE_VIEW, model, ConfigDetailsForGetNewInstanceTreeModel.class);
  }
  
}
