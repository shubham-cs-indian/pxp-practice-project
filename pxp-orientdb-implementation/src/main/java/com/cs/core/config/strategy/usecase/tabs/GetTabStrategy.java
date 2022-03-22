package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.GetTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetTabStrategy extends OrientDBBaseStrategy implements IGetTabStrategy {
  
  @Override
  public IGetTabModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_TAB, model, GetTabModel.class);
  }
}
