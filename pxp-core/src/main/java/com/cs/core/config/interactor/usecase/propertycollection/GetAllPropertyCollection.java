package com.cs.core.config.interactor.usecase.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;
import com.cs.core.config.propertycollection.IGetAllPropertyCollectionService;

@Service
public class GetAllPropertyCollection
    extends AbstractGetConfigInteractor<IGetAllPropertyCollectionRequestModel, IGetAllPropertyCollectionResponseModel>
    implements IGetAllPropertyCollection {
  
  @Autowired
  protected IGetAllPropertyCollectionService getAllPropertyCollectionService;
  
  @Override
  public IGetAllPropertyCollectionResponseModel executeInternal(IGetAllPropertyCollectionRequestModel getAllPropertyCollectionModel)
      throws Exception
  {
    return getAllPropertyCollectionService.execute(getAllPropertyCollectionModel);
  }
}
