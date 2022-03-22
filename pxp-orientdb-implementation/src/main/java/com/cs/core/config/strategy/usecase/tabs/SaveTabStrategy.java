package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.tabs.GetTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveTabStrategy extends OrientDBBaseStrategy implements ISaveTabStrategy {
  
  @Override
  public IGetTabModel execute(ISaveTabModel model) throws Exception
  {
    return execute(SAVE_TAB, model, GetTabModel.class);
  }
}
