package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.strategy.authorization.IDeletePartnerAuthorizationStrategy;

@Service
public class DeletePartnerAuthorization extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeletePartnerAuthorization {
  
  @Autowired
  protected IDeletePartnerAuthorizationStrategy deleteAuthorizationMappingService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return deleteAuthorizationMappingService.execute(model);
  }
}
