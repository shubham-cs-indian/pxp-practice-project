package com.cs.core.runtime.interactor.usecase.staticcollection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IDeleteStaticCollectionService;

@Service
public class DeleteStaticCollection extends AbstractRuntimeInteractor<IIdsListParameterModel, IBulkResponseModel>
    implements IDeleteStaticCollection {

  @Autowired
  protected IDeleteStaticCollectionService deleteStaticCollectcionService;
  
  @Override
  protected IBulkResponseModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return deleteStaticCollectcionService.execute(model);
  }
  
  
}  