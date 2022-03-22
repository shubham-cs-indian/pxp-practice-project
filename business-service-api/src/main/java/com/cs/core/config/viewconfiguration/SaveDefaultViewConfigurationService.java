package com.cs.core.config.viewconfiguration;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.strategy.usecase.viewconfiguration.ISaveDefaultViewConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Service
public class SaveDefaultViewConfigurationService extends AbstractSaveConfigService<IVoidModel, IViewConfigurationModel>
    implements ISaveDefaultViewConfigurationService {
  
  @Autowired
  protected ISaveDefaultViewConfigurationStrategy saveDefaultViewConfigurationStrategy;
  
  @Override
  public IViewConfigurationModel executeInternal(IVoidModel dataModel) throws Exception
  {
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(InitializeDataConstants.VIEW_CONFIGURATION);
    IViewConfigurationModel model = ObjectMapperUtil.readValue(stream, ViewConfigurationModel.class);
    return saveDefaultViewConfigurationStrategy.execute(model);
  }
}
