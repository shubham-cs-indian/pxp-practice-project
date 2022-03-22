package com.cs.core.config.sso;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;

public interface ISaveSSOConfigurationService extends
    ISaveConfigService<IListModel<ICreateSSOConfigurationModel>, IBulkSaveSSOConfigurationResponseModel> {
  
}
