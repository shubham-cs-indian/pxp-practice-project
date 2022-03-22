package com.cs.di.config.authorization;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;

public interface IBulkSavePartnerAuthorizationService extends IConfigService<IListModel<IIdLabelCodeModel>,IBulkSavePartnerAuthorizationModel> {
  
}
