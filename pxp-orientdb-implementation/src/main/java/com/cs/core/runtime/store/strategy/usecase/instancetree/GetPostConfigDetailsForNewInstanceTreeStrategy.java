package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.instancetree.GetPostConfigDetailsForNewInstanceTreeModel;

@Component
public class GetPostConfigDetailsForNewInstanceTreeStrategy extends OrientDBBaseStrategy
    implements IGetPostConfigDetailsForNewInstanceTreeStrategy {
  
  @Override
  public IGetPostConfigDetailsForNewInstanceTreeModel execute(IGetPostConfigDetailsRequestModel model) throws Exception
  {
    return execute(GET_POST_CONFIG_DETAILS_FOR_NEW_INSTANCE_TREE, model, GetPostConfigDetailsForNewInstanceTreeModel.class);
  }
  
  @Override
  public String getUsecase()
  {
    return "Post Config Details For Get Instance Tree Strategy";
  }
  
}
