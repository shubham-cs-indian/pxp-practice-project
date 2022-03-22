package com.cs.core.config.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;
import com.cs.core.config.strategy.usecase.propertycollection.IGetAllPropertyCollectionStrategy;

@Service
public class GetAllPropertyCollectionService
    extends AbstractGetConfigService<IGetAllPropertyCollectionRequestModel, IGetAllPropertyCollectionResponseModel>
    implements IGetAllPropertyCollectionService {
  
  @Autowired
  protected IGetAllPropertyCollectionStrategy getAllPropertyCollectionStrategy;
  
  @Override
  public IGetAllPropertyCollectionResponseModel executeInternal(IGetAllPropertyCollectionRequestModel getAllPropertyCollectionModel)
      throws Exception
  {
    return getAllPropertyCollectionStrategy.execute(getAllPropertyCollectionModel);
  }
}
