package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.GetTabModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateTabStrategy extends OrientDBBaseStrategy implements ICreateTabStrategy {
  
  @Override
  public IGetTabModel execute(ICreateTabModel model) throws Exception
  {
    return execute(CREATE_TAB, model, GetTabModel.class);
  }
}
