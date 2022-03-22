package com.cs.core.config.organization;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.ISaveOrganizationModel;
import com.cs.core.config.strategy.usecase.organization.ISaveOrganizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveOrganizationService extends AbstractSaveConfigService<ISaveOrganizationModel, IGetOrganizationModel>
    implements ISaveOrganizationService {
  
  @Autowired
  protected ISaveOrganizationStrategy saveOrganizationStrategy;
  
  @Override
  public IGetOrganizationModel executeInternal(ISaveOrganizationModel model) throws Exception
  {
    return saveOrganizationStrategy.execute(model);
  }
}
