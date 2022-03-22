package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.strategy.authorization.IDeletePartnerAuthorizationStrategy;

@Service
public class DeletePartnerAuthorizationService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeletePartnerAuthorizationService {
  
  @Autowired
  protected IDeletePartnerAuthorizationStrategy deleteAuthorizationMappingStrategy;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return deleteAuthorizationMappingStrategy.execute(model);
  }
}
