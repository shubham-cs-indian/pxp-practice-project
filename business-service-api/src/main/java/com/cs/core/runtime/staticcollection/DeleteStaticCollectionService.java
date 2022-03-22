package com.cs.core.runtime.staticcollection;


import org.springframework.stereotype.Service;

import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteStaticCollectionService extends AbstractDeleteStaticCollection<IIdsListParameterModel, IBulkResponseModel>
    implements IDeleteStaticCollectionService {
  
  @Override
  public CollectionType getCollectionType()
  {
    return CollectionType.staticCollection;
  }
  
}  