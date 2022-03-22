package com.cs.core.runtime.interactor.usecase.staticcollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.IMoveInstancesToCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IMoveInstancesToCollectionResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.staticcollection.IMoveInstancesToCollectionService;

@Service
public class MoveInstancesToCollection
    extends AbstractRuntimeInteractor<IMoveInstancesToCollectionModel, IMoveInstancesToCollectionResponseModel>
    implements IMoveInstancesToCollection {
  
  @Autowired
  protected IMoveInstancesToCollectionService moveInstancesToCollectionService;
  
  @Override
  public IMoveInstancesToCollectionResponseModel executeInternal(IMoveInstancesToCollectionModel dataModel) throws Exception
  {
    
    return moveInstancesToCollectionService.execute(dataModel);
  }
}
