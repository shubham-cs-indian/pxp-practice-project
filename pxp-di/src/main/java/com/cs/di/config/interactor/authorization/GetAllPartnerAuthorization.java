package com.cs.di.config.interactor.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.authorization.IGetAllPartnerAuthorizationService;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;

@Service
public class GetAllPartnerAuthorization
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllPartnerAuthorizationModel>
    implements IGetAllPartnerAuthorization {
  
  @Autowired
  protected IGetAllPartnerAuthorizationService getAllAuthorizationMappingService;

  @Override
  protected IGetAllPartnerAuthorizationModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getAllAuthorizationMappingService.execute(model);
  }
}

