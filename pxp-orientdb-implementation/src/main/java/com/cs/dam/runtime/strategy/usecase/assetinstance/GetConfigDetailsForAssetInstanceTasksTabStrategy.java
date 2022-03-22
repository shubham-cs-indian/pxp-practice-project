package com.cs.dam.runtime.strategy.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;

@Component
public class GetConfigDetailsForAssetInstanceTasksTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForAssetInstanceTasksTabStrategy {
  
  @Autowired
  protected ISessionContext context;
  
  @Override
  public IGetConfigDetailsForTasksTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    model.setUserId(context.getUserId());
    return execute(GET_CONFIG_DETAILS_FOR_ASSET_INSTANCE_TASKS_TAB, model, GetConfigDetailsForTasksTabModel.class);
  }
  
}
