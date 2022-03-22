package com.cs.core.runtime.staticcollection;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.collections.CollectionModel;
import com.cs.core.runtime.interactor.model.collections.GetStaticCollectionDetailsModel;
import com.cs.core.runtime.interactor.model.collections.ICollectionModel;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class GetStaticCollectionDetailsService
    extends AbstractRuntimeService<IIdParameterModel, IGetStaticCollectionDetailsModel>
    implements IGetStaticCollectionDetailsService {
  
  @Autowired
  protected RDBMSComponentUtils                 rdbmsComponentUtils;
  
  public IGetStaticCollectionDetailsModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    long parentCollectionIID = Long.parseLong(idModel.getId());
    ICollectionDTO parentCollectionRecordDTO = collectionDAO.getCollection(parentCollectionIID, CollectionType.staticCollection);
    List<ICollectionDTO> childCollectionRecordDTOs = collectionDAO.getAllCollections(parentCollectionIID, CollectionType.staticCollection);
    
    List<ICollectionModel> collectionList = new ArrayList<>();
    for(ICollectionDTO collectionDTO: childCollectionRecordDTOs) {
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
    
    IGetStaticCollectionDetailsModel returnModel = new GetStaticCollectionDetailsModel();
    returnModel.setId(Long.toString(parentCollectionRecordDTO.getCollectionIID()));
    returnModel.setLabel(parentCollectionRecordDTO.getCollectionCode());
    returnModel.setChildren(collectionList);
    return returnModel;
  }
}
