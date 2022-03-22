package com.cs.di.config.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;
import com.cs.di.config.strategy.authorization.ISavePartnerAuthorizationStrategy;

@Service
public class SavePartnerAuthorizationService extends AbstractSaveConfigService<ISavePartnerAuthorizationModel, IGetPartnerAuthorizationModel>
    implements ISavePartnerAuthorizationService {
  
  @Autowired
  protected ISavePartnerAuthorizationStrategy saveAuthorizationMappingStrategy;
  
  @Override
  protected IGetPartnerAuthorizationModel executeInternal(ISavePartnerAuthorizationModel model) throws Exception
  {
    return saveAuthorizationMappingStrategy.execute(model);
  }
}
