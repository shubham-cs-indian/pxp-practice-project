package com.cs.core.config.interactor.usecase.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.viewconfiguration.IGetViewConfigurationService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class GetViewConfiguration extends AbstractGetConfigInteractor<IVoidModel, IViewConfigurationModel>
    implements IGetViewConfiguration {
  
  @Autowired
  protected IGetViewConfigurationService getViewConfigurationService;
  
  @Override
  public IViewConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    return getViewConfigurationService.execute(dataModel);
  }
}