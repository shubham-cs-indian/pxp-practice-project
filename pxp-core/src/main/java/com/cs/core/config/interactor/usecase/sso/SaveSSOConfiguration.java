package com.cs.core.config.interactor.usecase.sso;

import com.cs.core.config.sso.ISaveSSOConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.strategy.usecase.sso.ISaveSSOConfigurationStrategy;

@Component
public class SaveSSOConfiguration extends
    AbstractSaveConfigInteractor<IListModel<ICreateSSOConfigurationModel>, IBulkSaveSSOConfigurationResponseModel>
    implements ISaveSSOConfiguration {
  
  @Autowired
  protected ISaveSSOConfigurationService saveSSOConfigurationService;
  
  @Override
  public IBulkSaveSSOConfigurationResponseModel executeInternal(
      IListModel<ICreateSSOConfigurationModel> dataModel) throws Exception
  {
    return saveSSOConfigurationService.execute(dataModel);
  }
}
