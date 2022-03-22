package com.cs.di.config.interactor.authorization;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.base.IDiInteractor;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;

public interface IBulkSavePartnerAuthorization extends IDiInteractor<IListModel<IIdLabelCodeModel>,IBulkSavePartnerAuthorizationModel> {
  
}
