package com.cs.pim.runtime.dynamiccollection;


import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.staticcollection.AbstractGetAllStaticCollection;

@Service
public class GetAllDynamicCollectionService
    extends AbstractGetAllStaticCollection<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetAllDynamicCollectionService {  
  
  @Override
  public CollectionType getCollectionType()
  {
    return CollectionType.dynamicCollection;
  }
}