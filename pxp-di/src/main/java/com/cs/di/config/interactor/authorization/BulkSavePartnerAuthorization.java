package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.config.authorization.IBulkSavePartnerAuthorizationService;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;

@Service
public class BulkSavePartnerAuthorization
    extends AbstractSaveConfigInteractor<IListModel<IIdLabelCodeModel>, IBulkSavePartnerAuthorizationModel>
    implements IBulkSavePartnerAuthorization {
  
  @Autowired
  protected IBulkSavePartnerAuthorizationService bulkSaveAuthorizationMappingService;
  
  @Override
  protected IBulkSavePartnerAuthorizationModel executeInternal(IListModel<IIdLabelCodeModel> authorizationMappingModel)
      throws Exception
  {
    return bulkSaveAuthorizationMappingService.execute(authorizationMappingModel);
  }
}
