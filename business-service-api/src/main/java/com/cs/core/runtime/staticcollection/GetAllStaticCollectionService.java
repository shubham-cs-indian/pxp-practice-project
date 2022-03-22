package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllStaticCollectionService
    extends AbstractGetAllStaticCollection<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetAllStaticCollectionService {
  
  
  @Override
  public CollectionType getCollectionType()
  {
    return CollectionType.staticCollection;
  }
  
  
 
}