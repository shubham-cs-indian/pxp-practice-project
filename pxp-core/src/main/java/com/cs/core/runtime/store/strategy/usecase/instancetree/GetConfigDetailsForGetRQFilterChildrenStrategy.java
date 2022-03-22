package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetRQFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenResponseModel;

@Component
public class GetConfigDetailsForGetRQFilterChildrenStrategy extends OrientDBBaseStrategy 
  implements IGetConfigDetailsForGetRQFilterChildrenStrategy {

  @Override
  public IConfigDetailsForGetRQFilterChildrenResponseModel execute(
      IConfigDetailsForGetRQFilterChildrenRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_RQ_FILTER_CHILDREN, model, ConfigDetailsForGetRQFilterChildrenResponseModel.class);
  }
  
}
