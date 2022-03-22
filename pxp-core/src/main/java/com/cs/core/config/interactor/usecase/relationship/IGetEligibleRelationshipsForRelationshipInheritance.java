package com.cs.core.config.interactor.usecase.relationship;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetEligibleRelationshipsForRelationshipInheritance extends
  IConfigInteractor<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>{
  
}
