package com.cs.core.config.sso;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.sso.IGetGridSSOConfigurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetGridSSOConfigurationService extends AbstractGetConfigService<IGetGridSSOConfigurationRequestModel, IGetGridSSOConfigurationResponseModel>
    implements IGetGridSSOConfigurationService {
  
  @Autowired
  protected IGetGridSSOConfigurationStrategy getGridSSOConfigurationStrategy;
  
  public IGetGridSSOConfigurationResponseModel executeInternal(
      IGetGridSSOConfigurationRequestModel dataModel) throws Exception
  {
    return getGridSSOConfigurationStrategy.execute(dataModel);
  }
}
