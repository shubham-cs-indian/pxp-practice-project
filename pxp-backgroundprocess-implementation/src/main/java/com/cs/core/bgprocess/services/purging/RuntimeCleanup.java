package com.cs.core.bgprocess.services.purging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.cs.bds.runtime.usecase.relationshipInheritance.RelationshipInheritance;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.RelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.IRuntimeCleanupModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.ConfigDetailsForRuntimeCleanUpResponseModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForRuntimeCleanUpResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.restore.RollbackAndRestoreUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@SuppressWarnings("unchecked")
public class RuntimeCleanup extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                             batchSize;
  protected int                           nbBatches      = 1;
  private int                             currentBatchNo = 0;
  IRelationshipInheritanceOnTypeSwitchDTO cleanupDTO     = new RelationshipInheritanceOnTypeSwitchDTO();
  RelationshipInheritance                 relationshipInheritance;
  ILocaleCatalogDAO                       localCatalogDAO;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    batchSize = CSProperties.instance()
        .getInt(propName("batchSize"));
    cleanupDTO.fromJSON(jobData.getEntryData()
        .toString());
    setCurrentBatchNo(currentBatchNo);
    jobData.getLog()
        .progress("Job started with iid=%d", jobData.getJobIID());
    RDBMSLogger.instance()
        .debug("End of initialization BGP %s / iid=%d / batchSize=%d ", getService(), getIID(),
            batchSize);
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    IBGProcessDTO.BGPStatus status = null;
    localCatalogDAO = openUserSession().openLocaleCatalog(userSession, cleanupDTO.getContentId());
    try {
      
      IConfigDetailsForRuntimeCleanUpResponseModel configDetails = getConfigDetails();
      
      executeEmbeddedRecordCleanup(configDetails);
      executeTaskRecordCleanup(configDetails);
      executeRelationshipRecordCleanup(configDetails);
      
      setCurrentBatchNo(++currentBatchNo);
      
      jobData.getProgress().setPercentageCompletion(100);
      
      if (jobData.getProgress().getPercentageCompletion() == 100) {
        status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
      }
      else {
        status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    return status;
  }
  
  protected IConfigDetailsForRuntimeCleanUpResponseModel getConfigDetails() throws JsonProcessingException, Exception
  {
    Map<String, Object> requestModel = new HashMap<>();
    
    requestModel.put(IRuntimeCleanupModel.KLASS_IDS, cleanupDTO.getExistingTypes());
    requestModel.put(IRuntimeCleanupModel.TAXONOMY_IDS, cleanupDTO.getExistingTaxonomies());
    requestModel.put(IRuntimeCleanupModel.REMOVED_KLASS_IDS, cleanupDTO.getRemovedTypes());
    requestModel.put(IRuntimeCleanupModel.REMOVED_TAXONOMY_IDS, cleanupDTO.getRemovedTaxonomies());
    
    JSONObject jsonObject = CSConfigServer.instance().request(requestModel, "GetConfigDetailsForRuntimeCleanup",
        localCatalogDAO.getLocaleCatalogDTO().getLocaleID());
    
    IConfigDetailsForRuntimeCleanUpResponseModel configDetails = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(jsonObject), ConfigDetailsForRuntimeCleanUpResponseModel.class);
    
    return configDetails;
  }
  
  protected void executeTaskRecordCleanup(IConfigDetailsForRuntimeCleanUpResponseModel configDetails) throws RDBMSException {
    
    try {
      List<String> taskIds = configDetails.getTaskIds();
      ITaskRecordDAO taskDAO = localCatalogDAO.openTaskDAO();
      
      if(taskIds != null && !taskIds.isEmpty()) {
        taskDAO.deleteTasksByTaskCodeAndEntityIID(taskIds, cleanupDTO.getContentId());
      }  
    }
    catch (Exception exception) {
      jobData.getLog().info("Error Occured in Task Cleanup");
    }
  }
  
  protected void executeEmbeddedRecordCleanup(
      IConfigDetailsForRuntimeCleanUpResponseModel configDetails)
      throws CSFormatException, Exception
  {
    try {
      if (!configDetails.getContextIds()
          .isEmpty()) {
        List<Long> variantEntityIIDs = localCatalogDAO
            .loadContextualEntityIIDs(configDetails.getContextIds(), cleanupDTO.getContentId());
        
        VariantInstanceUtils variantInstanceUtils = BGProcessApplication.getApplicationContext()
            .getBean(VariantInstanceUtils.class);
        
        IExceptionModel failure = new ExceptionModel();
        IProductDeleteDTO entryData = new ProductDeleteDTO();
        
        if (variantEntityIIDs != null && !variantEntityIIDs.isEmpty()) {
          
          variantInstanceUtils.deleteVariants(variantEntityIIDs, new ArrayList<>(), failure,
              entryData);
        }
      }
    }
    catch (Exception exception) {
      jobData.getLog().info("Error Occured in Embedded Objects Cleanup");
    }
  }
  
  protected void executeRelationshipRecordCleanup(
      IConfigDetailsForRuntimeCleanUpResponseModel configDetails) throws Exception
  {
    try {
      relationshipInheritance = BGProcessApplication.getApplicationContext()
          .getBean(RelationshipInheritance.class);
      
      Long baseEntityIId = cleanupDTO.getContentId();
      String baseEntityIIdStringValue = String.valueOf(baseEntityIId);
      IBaseEntityDTO baseEntityDTO = localCatalogDAO.getEntityByIID(baseEntityIId);
      Map<String, Object> relationshipSideIdVsReferencedRelationship = configDetails
          .getRelationshipSideIdVsReferencedRelationship();
      
      List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
      
      for (Entry<String, Object> relationship : relationshipSideIdVsReferencedRelationship.entrySet()) {
        
        Map<String, Object> refRelationship = (Map<String, Object>) relationship.getValue();
        
        List<Long> otherSideInstanceIds = localCatalogDAO.getOtherSideInstanceIIds(
            refRelationship.get(CommonConstants.SIDE_PROPERTY).toString(),
            baseEntityIIdStringValue, Long.valueOf(refRelationship.get(CommonConstants.PROPERTY_IID)
                .toString()));
        
        if (otherSideInstanceIds.isEmpty()) {
          continue;
        }
        
        String relationshipSideElementId = relationship.getKey();
        String relationshipCode = refRelationship.get(CommonConstants.CODE_PROPERTY).toString();
        IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
        
        modifiedRelationship.setId(relationshipCode);
        modifiedRelationship.setRelationshipId(relationshipCode);
        modifiedRelationship.setSideId(relationshipSideElementId);
        modifiedRelationship.setDeletedElements((List<String>) otherSideInstanceIds.stream()
            .map(otherSideEntityID -> String.valueOf(otherSideEntityID))
            .collect(Collectors.toList()));
        modifiedRelationship.setBaseType(BaseEntityUtils
            .getBaseTypeString(localCatalogDAO.getEntityByIID(otherSideInstanceIds.get(0))
                .getBaseType()));
        
        modifiedRelationships.add(modifiedRelationship);
      }
      
      if (!modifiedRelationships.isEmpty()) {
        
        BaseType baseType = baseEntityDTO.getBaseType();
        
        ISaveRelationshipInstanceModel saveRelationshipModel = new SaveRelationshipInstanceModel();
        saveRelationshipModel.setBaseType(BaseEntityUtils.getBaseTypeString(baseType));
        saveRelationshipModel.setId(baseEntityIIdStringValue);
        saveRelationshipModel.setModifiedRelationships(modifiedRelationships);
        saveRelationshipModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
        saveRelationshipModel.setTabId(SystemLevelIds.RELATIONSHIP_TAB);
       
        RollbackAndRestoreUtils rollbackAndRestoreUtils = BGProcessApplication.getApplicationContext()
            .getBean(RollbackAndRestoreUtils.class);
        rollbackAndRestoreUtils.executeSaveRelation(saveRelationshipModel, baseType);
      }
    }
    catch (Exception exception) {
      jobData.getLog()
          .info("Error Occured in Relationships Cleanup");
    }
  }
}
