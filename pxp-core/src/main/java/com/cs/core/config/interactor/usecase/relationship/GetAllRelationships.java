package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.relationship.IGetAllRelationshipsService;

@Service
public class GetAllRelationships extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllRelationshipsResponseModel>
    implements IGetAllRelationships {
  
  @Autowired
  protected IGetAllRelationshipsService getAllRelationshipsService;
  
  @Override
  public IGetAllRelationshipsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllRelationshipsService.execute(dataModel);
  }
}
