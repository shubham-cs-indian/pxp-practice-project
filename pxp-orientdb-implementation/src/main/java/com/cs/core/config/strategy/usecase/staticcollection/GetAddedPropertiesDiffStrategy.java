package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAddedPropertiesDiffStrategy extends OrientDBBaseStrategy
    implements IGetAddedPropertiesDiffStrategy {
  
  @Override
  public IListModel<IDefaultValueChangeModel> execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_ADDED_PROPERTIES_DIFF, model,
        new TypeReference<ListModel<DefaultValueChangeModel>>()
        {
          
        });
  }
}
