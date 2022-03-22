package com.cs.core.config.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetConfigDetailsForGetFilterChildrenStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetFilterChildrenStrategy {
  
  @Override
  public IConfigDetailsForGetFilterChildrenResponseModel execute(
      IConfigDetailsForGetFilterChildrenRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_FILTER_CHILDREN, model,ConfigDetailsForGetFilterChildrenResponseModel.class);
  }

}
