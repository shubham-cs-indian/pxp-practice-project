package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.di.config.authorization.ICreatePartnerAuthorizationService;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;

@Service
public class CreatePartnerAuthorization extends AbstractCreateConfigInteractor<IPartnerAuthorizationModel, IGetPartnerAuthorizationModel>
    implements ICreatePartnerAuthorization {
  
  @Autowired
  protected ICreatePartnerAuthorizationService createAuthorizationMappingService;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(IPartnerAuthorizationModel mappingModel) throws Exception
  {
    return createAuthorizationMappingService.execute(mappingModel);
  }
  
}
