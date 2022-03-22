package com.cs.core.config.interactor.usecase.propertycollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.propertycollection.ISavePropertyCollectionService;

@Service
public class SavePropertyCollection extends AbstractSaveConfigInteractor<ISavePropertyCollectionModel, IGetPropertyCollectionModel>
    implements ISavePropertyCollection {
  
  @Autowired
  protected ISavePropertyCollectionService savePropertyCollectionService;
  
  @Override
  public IGetPropertyCollectionModel executeInternal(ISavePropertyCollectionModel model) throws Exception
  {
    return savePropertyCollectionService.execute(model);
  }
}
