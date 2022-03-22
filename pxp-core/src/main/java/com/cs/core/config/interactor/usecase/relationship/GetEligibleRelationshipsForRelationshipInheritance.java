package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.relationship.IGetEligibleRelationshipsForRelationshipInheritanceService;

@Service
public class GetEligibleRelationshipsForRelationshipInheritance
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>
    implements IGetEligibleRelationshipsForRelationshipInheritance {
  
  @Autowired
  protected IGetEligibleRelationshipsForRelationshipInheritanceService getEligibleRelationshipsForRelationshipInheritanceService;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getEligibleRelationshipsForRelationshipInheritanceService.execute(dataModel);
  }
  
}
