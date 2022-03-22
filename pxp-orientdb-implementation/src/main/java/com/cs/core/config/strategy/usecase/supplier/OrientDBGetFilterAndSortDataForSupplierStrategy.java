package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.klass.GetFilterInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import org.springframework.stereotype.Component;

@Component("getFilterAndSortDataForSupplierStrategy")
public class OrientDBGetFilterAndSortDataForSupplierStrategy extends OrientDBBaseStrategy
    implements IGetFilterAndSortDataStrategy {
  
  @Override
  public IGetFilterInformationModel execute(IGetFilterAndSortDataRequestModel model)
      throws Exception
  {
    return execute(GET_FILTER_AND_SORT_DATA_FOR_SUPPLIER, model, GetFilterInformationModel.class);
  }
}
