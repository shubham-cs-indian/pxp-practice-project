package com.cs.core.bgprocess.services.asset;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BulkRelationshipCreateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IBulkRelationshipCreateDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceRelationshipsService;


public class BulkCreateRelationship extends AbstractBaseEntityProcessing {
  
  private IBulkRelationshipCreateDTO bulkRelationshoipCreateDTO = new BulkRelationshipCreateDTO();
 
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    bulkRelationshoipCreateDTO.fromJSON(initialProcessData.getEntryData().toString());
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException, CSFormatException
  {
    try {
      jobData.getLog().info("Creating Relationships.....");
      ISaveRelationshipInstanceModel requestModel = prepareSaveRelationshipInstanceModel();
      ISaveArticleInstanceRelationshipsService saveRelationshipInstance = BGProcessApplication.getApplicationContext().getBean(ISaveArticleInstanceRelationshipsService.class);
      saveRelationshipInstance.execute(requestModel);
    }
    catch (Exception e) {
      jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      throw new RDBMSException( 600, "ERROR", e);
    }
  }


  /**
   * Prepare SaveRelationshipInstanceModel getting data from DTO..
   * @return
   */
  private ISaveRelationshipInstanceModel prepareSaveRelationshipInstanceModel()
  {
    ISaveRelationshipInstanceModel dataModel = new SaveRelationshipInstanceModel();
    
    dataModel.setBaseType(bulkRelationshoipCreateDTO.getSide1BaseType());
    dataModel.setId(bulkRelationshoipCreateDTO.getSide1InstanceId());
    dataModel.setTabId(bulkRelationshoipCreateDTO.getTabId());
    dataModel.setTabType(bulkRelationshoipCreateDTO.getTabType());
    
    IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
    modifiedRelationship.setRelationshipId(bulkRelationshoipCreateDTO.getRelationshipId());
    modifiedRelationship.setId(bulkRelationshoipCreateDTO.getRelationshipEntityId());
    modifiedRelationship.setBaseType(AssetInstance.class.getCanonicalName());
    modifiedRelationship.setSideId(bulkRelationshoipCreateDTO.getSideId());
    
    List<IRelationshipVersion> addedElements = new ArrayList<>();
    for (Long assetIId : bulkRelationshoipCreateDTO.getSuccessInstanceIIds()) {
      IRelationshipVersion addedElement = new RelationshipVersion();
      addedElement.setId(assetIId.toString());
      addedElement.setTimeRange(new InstanceTimeRange());
      addedElements.add(addedElement);
    }
    modifiedRelationship.setAddedElements(addedElements);
    
    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<IContentRelationshipInstanceModel>();
    modifiedRelationships.add(modifiedRelationship);
    dataModel.setModifiedRelationships(modifiedRelationships);
    
    return dataModel;
  }
}
