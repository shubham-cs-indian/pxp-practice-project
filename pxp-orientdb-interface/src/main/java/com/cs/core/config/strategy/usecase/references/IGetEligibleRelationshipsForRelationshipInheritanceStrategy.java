package com.cs.core.config.strategy.usecase.references;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;


@Component
public interface IGetEligibleRelationshipsForRelationshipInheritanceStrategy extends 
  IConfigStrategy<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>{
  
}
