package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.strategy.usecase.relationship.IGetAllRelationshipsStrategy;

@Service
public class GetAllRelationshipsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllRelationshipsResponseModel>
    implements IGetAllRelationshipsService {
  
  @Autowired
  protected IGetAllRelationshipsStrategy getAllRelationshipsStrategy;
  
  @Override
  public IGetAllRelationshipsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllRelationshipsStrategy.execute(dataModel);
  }
}
