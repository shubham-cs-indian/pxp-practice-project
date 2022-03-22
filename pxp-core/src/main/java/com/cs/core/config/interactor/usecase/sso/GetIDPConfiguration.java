package com.cs.core.config.interactor.usecase.sso;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.sso.IGetIDPConfigurationResponseModel;
import com.cs.core.config.sso.IGetIDPConfigurationService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetIDPConfiguration 
    extends AbstractGetConfigInteractor<IModel, IGetIDPConfigurationResponseModel>
    implements IGetIDPConfiguration {

  @Autowired
  protected IGetIDPConfigurationService getIDPConfigurationService;


  @Override
  public IGetIDPConfigurationResponseModel executeInternal(IModel dataModel) throws Exception
  {
    return getIDPConfigurationService.execute(dataModel);
  }
}
