package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IGetAllStaticCollectionService;

@Service
public class GetAllStaticCollection extends AbstractRuntimeInteractor<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetAllStaticCollection {
  
  @Autowired
  protected IGetAllStaticCollectionService getAllStaticCollectionService;
  
  @Override
  protected IListModel<ICollectionModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllStaticCollectionService.execute(model);
  }
  
}