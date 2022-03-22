package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.strategy.authorization.ICreatePartnerAuthorizationStrategy;

@Service
public class CreatePartnerAuthorizationService extends AbstractCreateConfigService<IPartnerAuthorizationModel, IGetPartnerAuthorizationModel>
    implements ICreatePartnerAuthorizationService {
  
  @Autowired
  protected ICreatePartnerAuthorizationStrategy createAuthorizationMappingStrategy;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(IPartnerAuthorizationModel mappingModel) throws Exception
  {
    Validations.validateLabel(mappingModel.getLabel());
    return createAuthorizationMappingStrategy.execute(mappingModel);
  }
  
}
