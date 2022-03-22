package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.klass.GetFilterInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import org.springframework.stereotype.Component;

@Component("getFilterAndSortDataForAssetStrategy")
public class OrientDBGetFilterAndSortDataForAssetStrategy extends OrientDBBaseStrategy
    implements IGetFilterAndSortDataStrategy {
  
  @Override
  public IGetFilterInformationModel execute(IGetFilterAndSortDataRequestModel model)
      throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_FILTER_AND_SORT_DATA_FOR_ASSET, model,
        GetFilterInformationModel.class);
  }
}
