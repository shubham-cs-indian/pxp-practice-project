package com.cs.pim.runtime.dynamiccollection;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.collections.DynamicCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetBookmarkRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.staticcollection.CollectionValidations;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;

@Service
public class CreateDynamicCollectionService
    extends AbstractRuntimeService<IDynamicCollectionModel, IDynamicCollectionModel>
    implements ICreateDynamicCollectionService {

  
  @Autowired
  protected SearchAssembler       searchAssembler;
  
  @Autowired
  protected RDBMSComponentUtils   rdbmsUtils;
  
  @Autowired
  protected CollectionValidations collectionValidations;
  
  @Override
  public IDynamicCollectionModel executeInternal(IDynamicCollectionModel model) throws Exception
  {
    collectionValidations.validate(model);
    
    IGetBookmarkRequestModel bookmarkRequestModel = model.getGetRequestModel();
    
    IJSONContent searchCriteria = new JSONContent(ObjectMapperUtil.writeValueAsString(bookmarkRequestModel));
    
    ICollectionDTO newCollectionDTO = rdbmsUtils.newCollectionDTO(CollectionType.dynamicCollection,
        model.getLabel(), "", "-1", model.getIsPublic(), searchCriteria, new HashSet<Long>());
    ICollectionDAO collectionDAO = rdbmsUtils.getCollectionDAO();
    ICollectionDTO collectionDTO = collectionDAO.createCollection(newCollectionDTO);
    
    IDynamicCollectionModel prepareResponseModel = prepareResponseModel(collectionDTO);
    
    return prepareResponseModel;
  }

  
  public IDynamicCollectionModel prepareResponseModel(ICollectionDTO collectionDTO) {
    
    IDynamicCollectionModel dynamicCollectionModel = new DynamicCollectionModel();
    
    dynamicCollectionModel.setId(Long.toString(collectionDTO.getCollectionIID()));
    dynamicCollectionModel.setLabel(collectionDTO.getCollectionCode());
    dynamicCollectionModel.setType(Constants.DYNAMIC_COLLECTION_TYPE);
    dynamicCollectionModel.setIsPublic(collectionDTO.getIsPublic());
    dynamicCollectionModel.setCreatedOn(collectionDTO.getCreatedTrack().getWhen());
    dynamicCollectionModel.setCreatedBy(collectionDTO.getCreatedTrack().getWho());
    
    return dynamicCollectionModel;
  }  
}
