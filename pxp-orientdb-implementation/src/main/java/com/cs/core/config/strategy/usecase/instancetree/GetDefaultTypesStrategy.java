package com.cs.core.config.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.instancetree.GetDefaultTypesResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetDefaultTypesStrategy extends OrientDBBaseStrategy
    implements IGetDefaultTypesStrategy {
  
  @Override
  public IListModel<IGetDefaultTypesResponseModel> execute(IDefaultTypesRequestModel model)
      throws Exception
  {
    return execute(GET_DEFAULT_TYPES, model, new TypeReference<ListModel<GetDefaultTypesResponseModel>>() {});
  }
  
}
