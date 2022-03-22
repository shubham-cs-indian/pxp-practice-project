package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.di.config.authorization.ISavePartnerAuthorizationService;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;

@Service
public class SavePartnerAuthorization extends AbstractSaveConfigInteractor<ISavePartnerAuthorizationModel, IGetPartnerAuthorizationModel>
    implements ISavePartnerAuthorization {
  
  @Autowired
  protected ISavePartnerAuthorizationService saveAuthorizationMappingService;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(ISavePartnerAuthorizationModel model) throws Exception
  {
    return saveAuthorizationMappingService.execute(model);
  }
}
