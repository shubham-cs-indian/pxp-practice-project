package com.cs.core.config.interactor.usecase.sso;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.IBulkSaveSSOConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;

public interface ISaveSSOConfiguration extends
    ISaveConfigInteractor<IListModel<ICreateSSOConfigurationModel>, IBulkSaveSSOConfigurationResponseModel> {
  
}
