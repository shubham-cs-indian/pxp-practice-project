package com.cs.pim.runtime.dynamiccollection;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.collections.GetDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IGetDynamicCollectionModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.staticcollection.CollectionValidations;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;

@Service
public class SaveDynamicCollectionService extends AbstractRuntimeService<IDynamicCollectionModel, IGetDynamicCollectionModel>
    implements ISaveDynamicCollectionService {
  
  @Autowired
  protected SearchAssembler       searchAssembler;
  
  @Autowired
  protected RDBMSComponentUtils   rdbmsComponentUtils;
  
  @Autowired
  protected GetAllUtils           getAllUtils;
  
  @Autowired
  protected CollectionValidations collectionValidations;
  
  @Override
  public IGetDynamicCollectionModel executeInternal(IDynamicCollectionModel model) throws Exception
  {
    collectionValidations.validate(model);
    
    IGetDynamicCollectionModel responseModel = new GetDynamicCollectionModel();
    
    IGetInstanceTreeRequestModel instanceTreeRequestModel = model.getGetRequestModel();
    
    ICollectionDTO newCollectionDTO = rdbmsComponentUtils.newCollectionDTO(CollectionType.dynamicCollection, model.getLabel(), "", "-1",
        model.getIsPublic(), new JSONContent(), new HashSet<Long>());
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    
    if (instanceTreeRequestModel == null) {
      collectionDAO.changeViewPermission(newCollectionDTO, Long.parseLong(model.getId()));
      
    }
    else {
      IJSONContent searchCriteria = new JSONContent(ObjectMapperUtil.writeValueAsString(instanceTreeRequestModel));
      newCollectionDTO.setSearchCriteria(searchCriteria);
      collectionDAO.updateCollectionRecords(Long.parseLong(model.getId()), newCollectionDTO, new ArrayList<>(), new ArrayList<>());
    }
    ICollectionDTO collectionDTO = collectionDAO.getCollection(Long.parseLong(model.getId()), CollectionType.dynamicCollection);
    
    responseModel.setId(model.getId());
    responseModel.setLabel(collectionDTO.getCollectionCode());
    responseModel.setIsPublic(collectionDTO.getIsPublic());
    responseModel.setParentId(collectionDTO.getParentIID() == 0 ? "-1" : Long.toString(collectionDTO.getParentIID()));
    responseModel.setType(collectionDTO.getCollectionType().toString());
    responseModel.setCreatedOn(collectionDTO.getCreatedTrack().getWhen());
    responseModel.setCreatedBy(collectionDTO.getCreatedTrack().getWho());
    
    responseModel.setGetRequestModel(model.getGetRequestModel());
    responseModel.setFrom(model.getGetRequestModel() == null ? 0 : model.getGetRequestModel().getFrom());
    
    return responseModel;
  }
  
}
