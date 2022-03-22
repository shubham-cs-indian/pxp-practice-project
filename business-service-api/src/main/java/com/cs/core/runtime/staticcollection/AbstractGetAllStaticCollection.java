package com.cs.core.runtime.staticcollection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.collections.CollectionModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetAllStaticCollection<P extends IIdParameterModel, R extends IListModel<ICollectionModel>>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                 rdbmsComponentUtils;
  
  public abstract CollectionType getCollectionType();
  
  @Override
  public R executeInternal(IIdParameterModel model) throws Exception
  {
    CollectionType collectionType = getCollectionType();
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    
    List<ICollectionDTO> collectionRecords = collectionDAO.getAllCollections(Long.parseLong(model.getId()), collectionType);
    
    List<ICollectionModel> collectionList = new ArrayList<>();
    for(ICollectionDTO collectionDTO: collectionRecords) {
      ICollectionModel collection = new CollectionModel();
      collection.setId(Long.toString(collectionDTO.getCollectionIID()));;
      collection.setLabel(collectionDTO.getCollectionCode());
      collection.setCreatedBy(collectionDTO.getCreatedTrack().getWho());
      collection.setCreatedOn(collectionDTO.getCreatedTrack().getWhen());
      collection.setIsPublic(collectionDTO.getIsPublic());
      collection.setParentId("-1");
      collection.setType(collectionType.toString());
      
      collectionList.add(collection);
    }
    IListModel<ICollectionModel> returnModel = new ListModel<ICollectionModel>();
    returnModel.setList(collectionList);
    return  (R) returnModel;
  }

  
 
  
}
