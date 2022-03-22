package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetNewFilterAndSortDataForRQResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRQFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForRQResponseModel;

@Component
public class GetConfigDetailsForGetRQFilterAndSortDataStrategy extends OrientDBBaseStrategy 
  implements IGetConfigDetailsForGetRQFilterAndSortDataStrategy {

  @Override
  public IGetNewFilterAndSortDataForRQResponseModel execute(
      IConfigDetailsForRQFilterAndSortDataRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_RQ_FILTER_AND_SORT_DATA, model, GetNewFilterAndSortDataForRQResponseModel.class);
  }
  
}
