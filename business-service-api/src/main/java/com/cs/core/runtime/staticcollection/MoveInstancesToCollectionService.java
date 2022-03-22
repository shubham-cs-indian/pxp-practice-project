package com.cs.core.runtime.staticcollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.exception.collections.InstanceAlreadyExistsInCollectionException;
import com.cs.core.runtime.interactor.model.collections.IMoveInstancesToCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IMoveInstancesToCollectionResponseModel;
import com.cs.core.runtime.interactor.model.collections.MoveInstancesToCollectionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class MoveInstancesToCollectionService extends
    AbstractRuntimeService<IMoveInstancesToCollectionModel, IMoveInstancesToCollectionResponseModel> implements IMoveInstancesToCollectionService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  @Override
  public IMoveInstancesToCollectionResponseModel executeInternal(IMoveInstancesToCollectionModel dataModel) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    IMoveInstancesToCollectionResponseModel responseModel = new MoveInstancesToCollectionResponseModel();
    responseModel.setId(dataModel.getAddToCollectionId());
    List<Long> movedBaseEntityIIDs = new ArrayList<>();
    List<IIdAndTypeModel> baseEntityIIDs = dataModel.getAddedContents();
    for (IIdAndTypeModel idAndType : baseEntityIIDs) {
      movedBaseEntityIIDs.add(Long.parseLong(idAndType.getId()));
    }
    List<Long> entitiesToUpdateInElastic = new ArrayList<>(movedBaseEntityIIDs);
    Long addedToCollectionIID = dataModel.getAddToCollectionId() == null ? 0 : Long.parseLong(dataModel.getAddToCollectionId());
    Long removedFromCollectionIID = dataModel.getRemoveFromCollectionId() == null ? 0
        : Long.parseLong(dataModel.getRemoveFromCollectionId());
    
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    if (removedFromCollectionIID != 0) {
      ICollectionDTO collectionDTO = collectionDAO.getCollection(removedFromCollectionIID, CollectionType.staticCollection);
      collectionDAO.updateCollectionRecords(removedFromCollectionIID, collectionDTO, new ArrayList<>(), movedBaseEntityIIDs);
    }
    
    if (addedToCollectionIID != 0) {
      ICollectionDTO collectionDTO = collectionDAO.getCollection(addedToCollectionIID, CollectionType.staticCollection);
      responseModel.setLabel(collectionDTO.getCollectionCode());
      Boolean isExceptionGenerated = collectionDAO.updateCollectionRecords(addedToCollectionIID, collectionDTO, movedBaseEntityIIDs,
          new ArrayList<>());
      if (isExceptionGenerated) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new InstanceAlreadyExistsInCollectionException(), null, null);
      }
      Set<Long> linkedBaseEntityIIDs = collectionDTO.getLinkedBaseEntityIIDs();
      movedBaseEntityIIDs.removeAll(linkedBaseEntityIIDs);
    }
    
    List<String> successIDs = new ArrayList<>();
    for (Long id : movedBaseEntityIIDs) {
      successIDs.add(Long.toString(id));
    }
    
    for (Long entityId : entitiesToUpdateInElastic) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(entityId, IEventDTO.EventType.ELASTIC_UPDATE);
    }
    
    responseModel.setSuccessIds(successIDs);
    responseModel.setFailure(failure);
    return responseModel;
  }
}
