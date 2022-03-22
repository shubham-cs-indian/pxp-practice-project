package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;

@Component
public class GetConfigDetailsForOverviewTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForOverviewTabStrategy {
  
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_FOR_OVERVIEW_TAB, model,
        GetConfigDetailsForCustomTabModel.class);
  }
}
