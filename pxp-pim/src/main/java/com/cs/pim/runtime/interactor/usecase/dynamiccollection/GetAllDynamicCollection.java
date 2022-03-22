package com.cs.pim.runtime.interactor.usecase.dynamiccollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dynamiccollection.IGetAllDynamicCollectionService;

@Service
public class GetAllDynamicCollection extends AbstractRuntimeInteractor<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetAllDynamicCollection {
  
  @Autowired
  protected IGetAllDynamicCollectionService getAllDynamicCollectionService;
  
  @Override
  protected IListModel<ICollectionModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllDynamicCollectionService.execute(model);
  }
  
}