package com.cs.core.config.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.strategy.usecase.viewconfiguration.ISaveViewConfigurationStrategy;

@Service
public class SaveViewConfigurationService extends AbstractSaveConfigService<IViewConfigurationModel, IViewConfigurationModel>
    implements ISaveViewConfigurationService {
  
  @Autowired
  protected ISaveViewConfigurationStrategy saveViewConfigurationStrategy;
  
  @Override
  public IViewConfigurationModel executeInternal(IViewConfigurationModel dataModel) throws Exception
  {
    return saveViewConfigurationStrategy.execute(dataModel);
  }
}
