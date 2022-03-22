package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;

@Component
public class GetConfigDetailsForCustomTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForCustomTabStrategy {
  
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_CUSTOM_TAB, model,
        GetConfigDetailsForCustomTabModel.class);
  }
}
