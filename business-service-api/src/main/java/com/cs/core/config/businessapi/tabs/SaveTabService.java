package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.usecase.tabs.ISaveTabStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveTabService extends AbstractSaveConfigService<ISaveTabModel, IGetTabModel> implements ISaveTabService {
  
  @Autowired
  protected ISaveTabStrategy saveTabStrategy;
  
  @Autowired
  protected TabValidations tabValidations;
  
  @Override
  public IGetTabModel executeInternal(ISaveTabModel model) throws Exception
  {
    tabValidations.validate(model);
    return saveTabStrategy.execute(model);
  }
}
