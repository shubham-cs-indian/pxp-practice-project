package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.references.IGetEligibleRelationshipsForRelationshipInheritanceStrategy;

@Service
public class GetEligibleRelationshipsForRelationshipInheritanceService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>
    implements IGetEligibleRelationshipsForRelationshipInheritanceService {
  
  @Autowired
  protected IGetEligibleRelationshipsForRelationshipInheritanceStrategy getEligibleRelationshipsForRelationshipInheritanceStrategy;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getEligibleRelationshipsForRelationshipInheritanceStrategy.execute(dataModel);
  }
}
