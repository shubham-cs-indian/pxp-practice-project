package com.cs.core.config.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.strategy.usecase.viewconfiguration.IGetViewConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class GetViewConfigurationService extends AbstractGetConfigService<IVoidModel, IViewConfigurationModel>
    implements IGetViewConfigurationService {
  
  @Autowired
  protected IGetViewConfigurationStrategy getViewConfigurationStrategy;
  
  @Override
  public IViewConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    return getViewConfigurationStrategy.execute(dataModel);
  }
}