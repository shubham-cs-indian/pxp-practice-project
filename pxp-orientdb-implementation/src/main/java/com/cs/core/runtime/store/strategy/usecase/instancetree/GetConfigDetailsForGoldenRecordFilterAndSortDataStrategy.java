package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;

@Component
public class GetConfigDetailsForGoldenRecordFilterAndSortDataStrategy extends OrientDBBaseStrategy
  implements IGetConfigDetailsForGoldenRecordFilterAndSortDataStrategy {
  
@Override
public IGetNewFilterAndSortDataResponseModel execute(IConfigDetailsForFilterAndSortInfoRequestModel model)
  throws Exception
{
  return execute(GET_CONFIG_DETAILS_FOR_GOLDEN_RECORD_FILTER_AND_SORT_DATA, model, GetNewFilterAndSortDataResponseModel.class);
}

}