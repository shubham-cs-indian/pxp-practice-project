package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveSSOConfigurationStrategy extends
    IConfigStrategy<IListModel<ICreateSSOConfigurationModel>, IBulkSaveSSOConfigurationResponseModel> {
  
}
