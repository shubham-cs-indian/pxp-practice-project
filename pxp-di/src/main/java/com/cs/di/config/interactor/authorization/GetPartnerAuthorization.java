package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.authorization.IGetPartnerAuthorizationService;
import com.cs.di.config.model.authorization.GetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;

@Service
public class GetPartnerAuthorization extends AbstractGetConfigInteractor<IIdParameterModel, IGetPartnerAuthorizationModel>
    implements IGetPartnerAuthorization {
  
  @Autowired
  protected IGetPartnerAuthorizationService getAuthorizationMappingService;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(IIdParameterModel model) throws Exception
  {
    return model.getId() != null ? getAuthorizationMappingService.execute(model) : new GetPartnerAuthorizationModel();
  }
  
}
