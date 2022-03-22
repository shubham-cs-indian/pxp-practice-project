package com.cs.core.config.strategy.usecase.collection;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.collections.ConfigDetailsToGetCollectionInstancesResponseModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsToGetCollectionInstancesRequestModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsToGetCollectionInstancesResponseModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsToGetCollectionInstancesStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsToGetCollectionInstancesStrategy {
  
  public static final String useCase = "GetConfigDetailsToGetCollectionInstances";
  
  @Override
  public IConfigDetailsToGetCollectionInstancesResponseModel execute(
      IConfigDetailsToGetCollectionInstancesRequestModel model) throws Exception
  {
    return super.execute(useCase, model, ConfigDetailsToGetCollectionInstancesResponseModel.class);
  }
}
