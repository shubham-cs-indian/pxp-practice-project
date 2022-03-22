package com.cs.core.config.sso;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.strategy.usecase.sso.ISaveSSOConfigurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveSSOConfigurationService
    extends AbstractSaveConfigService<IListModel<ICreateSSOConfigurationModel>, IBulkSaveSSOConfigurationResponseModel>
    implements ISaveSSOConfigurationService {
  
  @Autowired
  protected ISaveSSOConfigurationStrategy saveSSOConfigurationStrategy;
  
  @Autowired
  protected SSOConfigurationValidations   ssoConfigurationValidations;
  
  @Override
  public IBulkSaveSSOConfigurationResponseModel executeInternal(
      IListModel<ICreateSSOConfigurationModel> dataModel) throws Exception
  {
    ssoConfigurationValidations.validate(dataModel);
    return saveSSOConfigurationStrategy.execute(dataModel);
  }
}
