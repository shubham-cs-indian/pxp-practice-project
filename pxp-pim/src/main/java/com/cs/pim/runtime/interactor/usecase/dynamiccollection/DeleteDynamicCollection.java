package com.cs.pim.runtime.interactor.usecase.dynamiccollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dynamiccollection.IDeleteDynamicCollectionService;

@Service
public class DeleteDynamicCollection extends AbstractRuntimeInteractor<IIdsListParameterModel, IBulkResponseModel>
    implements IDeleteDynamicCollection {
  
  @Autowired
  IDeleteDynamicCollectionService deleteDynamicCollectionService;
  
  @Override
  public IBulkResponseModel executeInternal(IIdsListParameterModel deleteModel) throws Exception
  {
    return deleteDynamicCollectionService.execute(deleteModel);
  }
  
}
