package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;
import com.cs.di.config.strategy.authorization.IGetAllPartnerAuthorizationStrategy;

@Service
public class GetAllPartnerAuthorizationService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllPartnerAuthorizationModel>
    implements IGetAllPartnerAuthorizationService {
  
  @Autowired
  protected IGetAllPartnerAuthorizationStrategy getAllAuthorizationMappingStrategy;

  @Override
  protected IGetAllPartnerAuthorizationModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getAllAuthorizationMappingStrategy.execute(model);
  }
}

