package com.cs.core.config.interactor.usecase.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.viewconfiguration.ISaveViewConfigurationService;

@Service
public class SaveViewConfiguration extends AbstractSaveConfigInteractor<IViewConfigurationModel, IViewConfigurationModel>
    implements ISaveViewConfiguration {
  
  @Autowired
  protected ISaveViewConfigurationService saveViewConfigurationService;
  
  @Override
  public IViewConfigurationModel executeInternal(IViewConfigurationModel dataModel) throws Exception
  {
    return saveViewConfigurationService.execute(dataModel);
  }
  
}
