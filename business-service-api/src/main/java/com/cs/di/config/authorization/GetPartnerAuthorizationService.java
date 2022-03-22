package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.model.authorization.GetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.strategy.authorization.IGetPartnerAuthorizationStrategy;

@Service
public class GetPartnerAuthorizationService extends AbstractGetConfigService<IIdParameterModel, IGetPartnerAuthorizationModel>
    implements IGetPartnerAuthorizationService {
  
  @Autowired
  protected IGetPartnerAuthorizationStrategy getAuthorizationMappingStrategy;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(IIdParameterModel model) throws Exception
  {
    return model.getId() != null ? getAuthorizationMappingStrategy.execute(model) : new GetPartnerAuthorizationModel();
  }
  
}
