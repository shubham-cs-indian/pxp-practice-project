package com.cs.di.config.strategy.authorization;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.base.IDiStrategy;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;

public interface IBulkSavePartnerAuthorizationStrategy
    extends IDiStrategy<IListModel<IIdLabelCodeModel>, IBulkSavePartnerAuthorizationModel> {
  
}
