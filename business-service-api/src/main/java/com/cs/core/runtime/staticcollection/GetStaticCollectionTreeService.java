package com.cs.core.runtime.staticcollection;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class GetStaticCollectionTreeService
    extends AbstractRuntimeService<IIdParameterModel, IListModel<ICollectionModel>>
    implements IGetStaticCollectionTreeService {
  
  @Autowired
  protected RDBMSComponentUtils                 rdbmsComponentUtils;
  
  
  public IListModel<ICollectionModel> executeInternal(IIdParameterModel idModel) throws Exception
  {
   
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    
    List<ICollectionDTO> collectionRecords = collectionDAO.getAllCollections(Long.parseLong(idModel.getId()), CollectionType.staticCollection);
    
    List<ICollectionModel> collectionList = new ArrayList<>();
    for(ICollectionDTO collectionDTO: collectionRecords) {
      ICollectionModel collection = new CollectionModel();
      collection.setId(Long.toString(collectionDTO.getCollectionIID()));;
      collection.setLabel(collectionDTO.getCollectionCode());
      collection.setCreatedBy(collectionDTO.getCreatedTrack().getWho());
      collection.setCreatedOn(collectionDTO.getCreatedTrack().getWhen());
      collection.setIsPublic(collectionDTO.getIsPublic());
      collection.setParentId(Long.toString(collectionDTO.getParentIID()));
      collection.setType(CollectionType.staticCollection.toString());
      
      collectionList.add(collection);
    }
    IListModel<ICollectionModel> returnModel = new ListModel<ICollectionModel>();
    returnModel.setList(collectionList);
    return returnModel;
    
  }

}
