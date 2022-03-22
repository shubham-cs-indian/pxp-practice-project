package com.cs.core.config.interactor.usecase.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.viewconfiguration.ISaveDefaultViewConfigurationService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class SaveDefaultViewConfiguration extends AbstractSaveConfigInteractor<IVoidModel, IViewConfigurationModel>
    implements ISaveDefaultViewConfiguration {
  
  @Autowired
  protected ISaveDefaultViewConfigurationService saveDefaultViewConfigurationService;
  
  @Override
  public IViewConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    return saveDefaultViewConfigurationService.execute(dataModel);
  }
  
}
