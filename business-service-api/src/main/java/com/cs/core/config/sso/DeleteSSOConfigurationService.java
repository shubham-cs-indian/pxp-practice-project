package com.cs.core.config.sso;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.strategy.usecase.sso.IDeleteSSOConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteSSOConfigurationService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSSOConfigurationService {
  
  @Autowired
  protected IDeleteSSOConfigurationStrategy deleteSSOConfigurationStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteSSOConfigurationStrategy.execute(dataModel);
  }
}
