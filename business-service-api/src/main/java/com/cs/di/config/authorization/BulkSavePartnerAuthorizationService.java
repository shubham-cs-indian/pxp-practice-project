package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;
import com.cs.di.config.strategy.authorization.IBulkSavePartnerAuthorizationStrategy;

@Service
public class BulkSavePartnerAuthorizationService
    extends AbstractSaveConfigService<IListModel<IIdLabelCodeModel>, IBulkSavePartnerAuthorizationModel>
    implements IBulkSavePartnerAuthorizationService {
  
  @Autowired
  protected IBulkSavePartnerAuthorizationStrategy bulkSaveAuthorizationMappingStrategy;
  
  @Override
  protected IBulkSavePartnerAuthorizationModel executeInternal(IListModel<IIdLabelCodeModel> authorizationMappingModel)
      throws Exception
  {
    return bulkSaveAuthorizationMappingStrategy.execute(authorizationMappingModel);
  }
}
