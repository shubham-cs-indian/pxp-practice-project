package com.cs.core.bgprocess.services.datatransfer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.core.bgprocess.dto.RelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IRelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.coupling.dto.BGPCouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class RelationshipDataTransferOnConfigChange extends AbstractBGProcessJob implements IBGProcessJob{

  private String[]           stepLabels             = { "AddedElements", "DeletedElements", "ModifiedElements" };
  IRelationshipDataTransferOnConfigChangeDTO relationshipDataTransferOnConfigChangeDTO = new RelationshipDataTransferOnConfigChangeDTO();
  protected int                nbBatches                   = 1;
  protected int                batchSize;
  int                          currentBatchNo              = 0;
  RelationshipDataTransfer dataTransfer = new RelationshipDataTransfer();
  List<Long> side1PropertyIDs = relationshipDataTransferOnConfigChangeDTO.getSide1AddedPropertyIIDs();
  List<Long> side2PropertyIDs = relationshipDataTransferOnConfigChangeDTO.getSide2AddedPropertyIIDs();

  
  private enum STEPS
  {
    ADDED_ELEMENTS, DELETED_ELEMENTS, MODIFIED_ELEMENTS;
    
    static STEPS valueOf(int i)
    {
      return STEPS.values()[i];
    }
  }
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    relationshipDataTransferOnConfigChangeDTO.fromJSON(jobData.getEntryData().toString());
    this.initProgressData();
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  protected void initProgressData()
  {
    if (jobData.getProgress().getStepNames().size() == 1) {
      jobData.getProgress().initSteps(stepLabels);
    }
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    STEPS currentStep = STEPS.valueOf(currentStepNo - 1);
    
    switch (currentStep) {
      case ADDED_ELEMENTS:
        handleAddedElements();
        break;
      case DELETED_ELEMENTS:
        handleDeletedElements();
        break;
      case MODIFIED_ELEMENTS:
        handleModifiedElements();
        break;
      default:
        throw new RDBMSException(100, "Programm Error", "Unexpected step-no: " + currentStepNo);
    }
    
    jobData.getProgress().incrStepNo();
    
    // Return of status
    IBGProcessDTO.BGPStatus status = null;
    if (jobData.getProgress().getPercentageCompletion() == 100)
      status = jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    else
      status = BGPStatus.RUNNING;
    
    return status;
  }
  
  public void handleModifiedElements() throws Exception{
    
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
    List<IModifiedPropertiesCouplingDTO> modifiedProperties = relationshipDataTransferOnConfigChangeDTO.getModifiedProperties();
    
    ICouplingDTO couplingDTOToRaiseEvent = null;
    Set<Long> consolidatedTargetEntityIIds = new HashSet<>();
    for(IModifiedPropertiesCouplingDTO couplingDTO : modifiedProperties) { 
      
      long propertyIID = couplingDTO.getPropertyIID();
      long relationshipIID = relationshipDataTransferOnConfigChangeDTO.getRelationshipIID();
      CouplingBehavior couplingBehavior = CouplingBehavior.valueOf(couplingDTO.getCouplingType()) == CouplingBehavior.DYNAMIC ? CouplingBehavior.TIGHTLY : CouplingBehavior.DYNAMIC;
      
      List<ICouplingDTO> targetCouplingDTOs = couplingDAO.
          getConflictingValuesByCouplingSourceIIDAndPropertyIID(relationshipIID, propertyIID);
      
      if(!targetCouplingDTOs.isEmpty()) {
        couplingDTOToRaiseEvent = targetCouplingDTOs.get(0);
      }
      
      for(ICouplingDTO targetCouplingDTO : targetCouplingDTOs) {
        
        if(couplingBehavior == CouplingBehavior.DYNAMIC) {
          RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
          
          switch(recordStatus) {
            
            case COUPLED:
              configModificationForDynamicCoupledRecord(localeCatlogDAO, couplingDAO, propertyIID, relationshipIID,
                  targetCouplingDTO);
              break;
              
            case FORKED:
              configModificationForDynamicForkedRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case NOTIFIED:
              configModificationForDynamicNotifiedRecord(localeCatlogDAO, couplingDAO, propertyIID, relationshipIID,
                  targetCouplingDTO);
              break;
            default:
              break;
          }
        }else {
          RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
          
          switch(recordStatus) {
            
            case COUPLED:
              configModificationForTightlyCoupledRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case FORKED:
              configModificationForTightlyForkedRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case NOTIFIED:
              configModificationForTightlyNotifiedRecord(couplingDAO, targetCouplingDTO);
              break;
            default:
              break;
          }
        }
        consolidatedTargetEntityIIds.add(targetCouplingDTO.getTargetEntityIID());
        consolidatedTargetEntityIIds.add(targetCouplingDTO.getSourceEntityIID());
      }
    }
    
    for(Long targetEntityIID : consolidatedTargetEntityIIds) {
      IBaseEntityDAO targetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID);
      IConfigDetailsForBulkPropagationResponseModel configDetails = DataTransferUtil.getConfigDetailForBaseEntity(targetEntityDAO);
      
      RuleHandler ruleHandler = new RuleHandler();
      IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(configDetails.getReferencedElements(),
          configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
      ruleHandler.evaluateDQMustShould(targetEntityIID, dataRules, targetEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
    }
    
    if(couplingDTOToRaiseEvent != null) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(couplingDTOToRaiseEvent.getSourceEntityIID(), 
          IEventDTO.EventType.END_OF_TRANSACTION);
    }
  }

  /**
   * Checks wheather there are multiple Dynamic coupled sources of different relationship
   * if multiple sources are there then just marked current source as forked 
   * if no multiple sources then marked as forked
   */
  private void configModificationForDynamicForkedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws RDBMSException
  {
    
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    
    if (sourceConflictingValues.size() > 0) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
    }
    else {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
    }
  }

  /**
   * Checks wheather there are multiple sources for same relationship
   * If 1 source is found than current record is marked as coupled.
   * If multiple sources are found than current record is marked as notified
   */
  private void configModificationForTightlyCoupledRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO)
      throws RDBMSException, Exception
  {
    List<ICouplingDTO> sourceValuesForSameRelationship = couplingDAO.getSourceConflictingValuesForRelationship(targetCouplingDTO);
    
    if(sourceValuesForSameRelationship.size() == 1) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
      couplingDAO.updateCouplingTypeForCoupledRecord(targetCouplingDTO, RecordStatus.COUPLED);
    }else if(sourceValuesForSameRelationship.size() > 1) {
      
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), 
          targetCouplingDTO.getTargetEntityIID(), targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
      
      createPropertyRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID());
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED); 
    }
  }

  private void configModificationForTightlyNotifiedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws CSFormatException, Exception
  {
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
    
    for(ICouplingDTO sourceValues : sourceConflictingValues) {
      couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
    }
    
    List<ICouplingDTO> sourceConflictingForSameRelationship = couplingDAO.getSourceConflictingValuesForRelationship(targetCouplingDTO);
    
    if(sourceConflictingForSameRelationship.size() == 1) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
      //couplingDAO.resolvedConflicts(targetCouplingDTO);
      
      resolveConflictsAndRaiseEventsForWorkFlow(rdbmsComponentUtils.getLocaleCatlogDAO(), couplingDAO, 
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO);
      
      deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
      
    }else if(sourceConflictingForSameRelationship.size() > 1) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
    }
  }

  private void configModificationForTightlyForkedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws Exception
  {
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    if(sourceConflictingValues.size() > 0) {
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
    }else {
      
      List<ICouplingDTO> sourceConflictsForSameRelationship = couplingDAO.getSourceConflictingValuesForRelationship(targetCouplingDTO);
      
      List<ICouplingDTO> sourceTightlyConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
      if(sourceConflictsForSameRelationship.size() == 1) {
        
        for(ICouplingDTO sourceValues : sourceTightlyConflictingValues) {
          
          couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
          couplingDAO.deleteCoupledRecord(sourceValues.getSourceEntityIID(), sourceValues.getTargetEntityIID(),
              sourceValues.getPropertyIID(), targetCouplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
        }
        
        targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
        //couplingDAO.resolvedConflicts(targetCouplingDTO);
        
        resolveConflictsAndRaiseEventsForWorkFlow(rdbmsComponentUtils.getLocaleCatlogDAO(), couplingDAO, 
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO);
        
        deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
        
      }else if(sourceConflictsForSameRelationship.size() > 1){
        
        for(ICouplingDTO sourceValues : sourceTightlyConflictingValues) {
          List<ICouplingDTO> coupledRecord = couplingDAO.getCoupledRecordForDynamicCoupled(sourceValues.getTargetEntityIID(), 
              sourceValues.getSourceEntityIID() ,sourceValues.getPropertyIID(), sourceValues.getCouplingSourceIID(), 0l);
          
          if(coupledRecord.size() > 0) {
            ICouplingDTO iCouplingDTO = coupledRecord.get(0);
            createPropertyRecord(iCouplingDTO.getSourceEntityIID(), iCouplingDTO.getTargetEntityIID(), 
                iCouplingDTO.getPropertyIID(), iCouplingDTO.getLocaleIID());
            
            couplingDAO.deleteCoupledRecord(sourceValues.getSourceEntityIID(), sourceValues.getTargetEntityIID(),
                sourceValues.getPropertyIID(), targetCouplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
          }
          couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
        }
        
        targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_RELATIONSHIP);
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      }
    }
  }

  private void deletePropertyRecordFromTargetBaseEntity(ICouplingDTO targetCouplingDTO) throws Exception, RDBMSException, CSFormatException
  {
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    if(targetCouplingDTO.getLocaleIID() == 0l) {
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID());
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID()));
      couplingDAO.deletePropertyRecord(targetBaseEntityDTO.getPropertyRecord(targetCouplingDTO.getPropertyIID()));
    }else {
      String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(targetCouplingDTO.getLocaleIID()).getLanguageCode();
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID(), languageCode);
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID()));
      couplingDAO.deletePropertyRecord(targetBaseEntityDTO.getPropertyRecord(targetCouplingDTO.getPropertyIID()));
    }
  }

  /**
   * Checks wheather there are mutiple Dynamic Coupled sources of Record Status Notified for different relationship
   * If 1 Source is there than it will be marked as coupled and current record will be marked as forked
   * If multiple source is there than current record will be marked as Forked
   * If no sources are found than current record will be marked as notified.
   * @throws Exception 
   * @throws CSFormatException 
   */
  
  private void configModificationForDynamicNotifiedRecord(ILocaleCatalogDAO localeCatlogDAO, ICouplingDAO couplingDAO,
      long propertyIID, long relationshipIID, ICouplingDTO targetCouplingDTO) throws CSFormatException, Exception
  {

    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValuesForRecordStatus(targetCouplingDTO, 
        CouplingBehavior.DYNAMIC, RecordStatus.NOTIFIED);
    
    if(sourceConflictingValues.size() == 1) {
      resolveConflictsAndRaiseEventsForWorkFlow(localeCatlogDAO, couplingDAO, propertyIID, sourceConflictingValues.get(0));
      
      deletePropertyRecordFromTargetBaseEntity(sourceConflictingValues.get(0));
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
    }else if(sourceConflictingValues.size() > 1) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED); 
    }else if(sourceConflictingValues.size() == 0) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
    }
    
  }

  private void resolveConflictsAndRaiseEventsForWorkFlow(ILocaleCatalogDAO localeCatlogDAO, ICouplingDAO couplingDAO, long propertyIID,
     ICouplingDTO couplingDTO) throws RDBMSException, Exception, CSFormatException
  {
    String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(couplingDTO.getLocaleIID()).getLanguageCode();
    
    IBaseEntityDAO targetBaseEntityDAO = languageCode != null ? getBaseEntityDAO(couplingDTO.getTargetEntityIID(), languageCode) : 
      rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getTargetEntityIID());
    
    couplingDAO.resolvedConflicts(couplingDTO);
    
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
    IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(propertyDTO);
    IPropertyRecordDTO propertyRecord = targetBaseEntityDTO.getPropertyRecord(propertyIID);
    
    couplingDAO.raiseEventsForCoupling(propertyRecord, couplingDTO, localeCatlogDAO, targetBaseEntityDTO);
    
  }

  /**
   * Checks wheather there are dynamic coupled sources coming from different relationship 
   * if there is 1 dynamic coupled source it will be marked as coupled
   * if there is more than 1 dynamic coupled sources than they will be marked as notified
   * if there is no dynamic coupled sources than it will be check for multiple source for the same relationship.
   */
  
  private void configModificationForDynamicCoupledRecord(ILocaleCatalogDAO localeCatlogDAO, ICouplingDAO couplingDAO,
       long propertyIID, long relationshipIID, ICouplingDTO targetCouplingDTO) throws RDBMSException, Exception
  {
    
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, 
        CouplingBehavior.DYNAMIC);
    
    if(sourceConflictingValues.size() == 1) {
      
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), 
          targetCouplingDTO.getTargetEntityIID(), targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(),
          rdbmsComponentUtils.getLocaleCatlogDAO());
      
      resolveConflictsAndRaiseEventsForWorkFlow(localeCatlogDAO, couplingDAO, propertyIID, sourceConflictingValues.get(0));
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
    }else if(sourceConflictingValues.size() > 1) {
      
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), 
          targetCouplingDTO.getTargetEntityIID(), targetCouplingDTO.getPropertyIID(),
          targetCouplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
      
      createPropertyRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID());
      
      for(ICouplingDTO sourceCouplingDTO : sourceConflictingValues) {
        couplingDAO.updateRecordStatusForConflictingValues(sourceCouplingDTO, RecordStatus.NOTIFIED);
      }
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
      couplingDAO.updateRecordStatusForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);

    }else if(sourceConflictingValues.size() == 0) {
      
      List<ICouplingDTO> sourceValuesForSameRelationship = couplingDAO.getSourceConflictingValuesForRelationship(targetCouplingDTO);
      
      if(sourceValuesForSameRelationship.size() == 1) {
        targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
        
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
        couplingDAO.updateCouplingTypeForCoupledRecord(targetCouplingDTO, RecordStatus.COUPLED);
        
      }else if(sourceValuesForSameRelationship.size() > 1) {
        
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), 
            targetCouplingDTO.getTargetEntityIID(), targetCouplingDTO.getPropertyIID(),
            targetCouplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
        
        createPropertyRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID());
        
        targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_RELATIONSHIP);
        
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      }
    }
  }
  
  private void handleDeletedElements() throws Exception
  {
    Long relationshipIID = relationshipDataTransferOnConfigChangeDTO.getRelationshipIID();
    List<Long> deletedPropertyIIDs = relationshipDataTransferOnConfigChangeDTO.getDeletedPropertyIIDs();
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    for (Long deletedPropertyIID : deletedPropertyIIDs) {
      List<ICouplingDTO> conflictingValues = couplingDAO.getConflictsCoupledRecord(relationshipIID, deletedPropertyIID);
      for (ICouplingDTO couplingDTO : conflictingValues) {
        
        createPropertyRecord(couplingDTO.getSourceEntityIID(), couplingDTO.getTargetEntityIID(), 
            couplingDTO.getPropertyIID(), couplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(couplingDTO.getSourceEntityIID(), couplingDTO.getTargetEntityIID(), 
            couplingDTO.getPropertyIID(), couplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
      }
      couplingDAO.deleteConflictingValues(deletedPropertyIID, relationshipIID);
    }
  }
  
  public void createPropertyRecord(long sourceEntityIID, long targetEntityIID, long propertyIID, long localeIID) throws Exception
  {
    String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode();
    IBaseEntityDAO sourceEntityDAO = getBaseEntityDAO(sourceEntityIID, languageCode);
    LocaleCatalogDAO localeCatlogDAO = (LocaleCatalogDAO) rdbmsComponentUtils.getLocaleCatlogDAO();
    IPropertyDTO propertyDTO = localeCatlogDAO.getPropertyByIID(propertyIID);
    IBaseEntityDTO sourceBaseEntityDTO = sourceEntityDAO.loadPropertyRecords(propertyDTO);
    IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(propertyIID);
    
    IBaseEntityDAO targetBaseEntityDAO = getBaseEntityDAO(targetEntityIID, languageCode);
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
      
      if(languageCode.equals(null)) {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue()).build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }else {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue())
            .localeID(languageCode)
            .build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }
    }
    else {
      ITagsRecordDTO tagRecord = (ITagsRecordDTO) propertyRecord;
      ITagsRecordDTO newTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
      Set<ITagDTO> newTagsDTO = tagRecord.getTags();
      newTagRecordDTO.setTags(newTagsDTO.toArray(new ITagDTO[newTagsDTO.size()]));
      targetBaseEntityDAO.createPropertyRecords(newTagRecordDTO);
    }
  }
 
  public IBaseEntityDAO getBaseEntityDAO(long entityIID, String localeCode) throws Exception
  {
    
    if (localeCode.equals(null)) {
      return rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    }
    return rdbmsComponentUtils.getBaseEntityDAO(entityIID, localeCode);
  }
  
  private void handleAddedElements() throws Exception
  {
    List<IBGPCouplingDTO> couplingDTOForAddedProperties = getRelationshipInfo(relationshipDataTransferOnConfigChangeDTO.getRelationshipIID(), relationshipDataTransferOnConfigChangeDTO.getIsNature());
    if(couplingDTOForAddedProperties.isEmpty()) {
      return;
    }
    IBGPCouplingDTO ibgpCouplingDTO = couplingDTOForAddedProperties.get(0);
    List<String> relationshipIds = new ArrayList<>();
    List<String> natureRelationshipIds = new ArrayList<>();
    String relationshipId;
    if (ibgpCouplingDTO.getRelationshipId() != null) {
      relationshipId = ibgpCouplingDTO.getRelationshipId();
      relationshipIds.add(relationshipId);
    }
    else {
      relationshipId = ibgpCouplingDTO.getNatureRelationishipId();
      natureRelationshipIds.add(relationshipId);
    }
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetails(relationshipIds, natureRelationshipIds);
    
    IRelationship relationshipConfig = configDetails
        .getReferencedRelationships()
        .get(relationshipId);
    if (relationshipConfig == null) {
      relationshipConfig = configDetails
          .getReferencedNatureRelationships()
          .get(relationshipId);
    }
    
    String sideId = relationshipConfig.getSide1().getElementId();
    
    RelationSide relationshipSide = DataTransferUtil.getRelationshipSide(sideId,
        relationshipConfig);
    
    IPropertyDTO relationshipPropertyDTO = RDBMSUtils.newPropertyDTO(
        relationshipConfig.getPropertyIID(), relationshipConfig.getId(),
        relationshipConfig.getCode(), PropertyType.RELATIONSHIP);
    relationshipPropertyDTO.setRelationSide(relationshipSide);
    
    IReferencedRelationshipPropertiesModel referencedRelationshipProperties = configDetails
        .getReferencedRelationshipProperties()
        .get(relationshipId);
    
    IRelationshipSidePropertiesModel holderSideProperties = referencedRelationshipProperties.getSide1();
    IRelationshipSidePropertiesModel oppositeSideProperties = referencedRelationshipProperties.getSide2();
    
    fillFilteredAddedSideProperties(holderSideProperties, side1PropertyIDs);
    fillFilteredAddedSideProperties(oppositeSideProperties, side2PropertyIDs);
    
    for (IBGPCouplingDTO couplingDTO : couplingDTOForAddedProperties) {
      
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getSourceBaseEntityIIDs().get(0));
      DataTransferUtil.handleHolderSideCouplingProperties(baseEntityDAO, holderSideProperties, 
          relationshipPropertyDTO, couplingDTO.getAddedEntityIIDs(), couplingDTO.getDeletedEntityIIDs());
      DataTransferUtil.handleOppositeSideCouplingProperties(baseEntityDAO, oppositeSideProperties, 
          relationshipPropertyDTO, couplingDTO.getDeletedEntityIIDs(), couplingDTO.getAddedEntityIIDs());
      
    }
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(couplingDTOForAddedProperties.get(0).getSourceBaseEntityIIDs().get(0), 
        IEventDTO.EventType.END_OF_TRANSACTION);
    
  }

private void fillFilteredAddedSideProperties(IRelationshipSidePropertiesModel sideProperties, List<Long> addedPropertyIIDs)
{
  List<IReferencedRelationshipProperty> attributes = sideProperties.getAttributes();
  List<IReferencedRelationshipProperty> dependentAttributes = sideProperties.getDependentAttributes();
  List<IReferencedRelationshipProperty> tags = sideProperties.getTags();
  List<IReferencedRelationshipProperty> filteredAttributes = new ArrayList<>();
  List<IReferencedRelationshipProperty> filteredTags = new ArrayList<>();
  List<IReferencedRelationshipProperty> filteredDependentAttributes = new ArrayList<>();
  attributes.stream().filter(x -> addedPropertyIIDs.contains(x.getPropertyIID())).forEach(x -> filteredAttributes.add(x));
  tags.stream().filter(x -> addedPropertyIIDs.contains(x.getPropertyIID())).forEach(x -> filteredTags.add(x));
  dependentAttributes.stream().filter(x -> addedPropertyIIDs.contains(x.getPropertyIID())).forEach(x -> filteredDependentAttributes.add(x));
  
  sideProperties.setAttributes(filteredAttributes);
  sideProperties.setDependentAttributes(filteredDependentAttributes);
  sideProperties.setTags(filteredTags);
}

private static final String GET_RELATIONS = " select * from pxp.relation where propertyiid = ?";
  
  public List<IBGPCouplingDTO> getRelationshipInfo(Long relationshipIID, Boolean isNature) throws SQLException, RDBMSException
  {
    List<IBGPCouplingDTO> couplingDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuilder query = new StringBuilder(GET_RELATIONS);
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.setLong(1, relationshipIID);
      stmt.execute();
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        IBGPCouplingDTO dto = new BGPCouplingDTO();
        List<Long> sourceEntityIIDs = new ArrayList<>();
        sourceEntityIIDs.add(ruleResult.getLong("side1entityiid"));
        List<Long> targetEntityIIDs = new ArrayList<>();
        targetEntityIIDs.add(ruleResult.getLong("side2entityiid"));
        dto.setSourceBaseEntityIIDs(sourceEntityIIDs);
        dto.setAddedEntityIIDs(targetEntityIIDs);
        if(isNature) {
          dto.setNatureRelationshipId(ConfigurationDAO.instance().getPropertyByIID(ruleResult.getLong("propertyiid")).getCode());
        }
        else {
          dto.setRelationshipId(ConfigurationDAO.instance().getPropertyByIID(ruleResult.getLong("propertyiid")).getCode());
        }
        couplingDTOs.add(dto);
      }
    });
    return couplingDTOs;
  } 
  
  @SuppressWarnings("unchecked")
  private IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails(List<String> relationshipIds,
      List<String> natureRelationshipIds)
      throws CSFormatException, CSInitializationException, Exception, JsonProcessingException
  {
    Map<String, Object> configRequestModel = new HashedMap<>();
    configRequestModel.put("relationshipIds", relationshipIds);
    configRequestModel.put("natureRelationshipIds", natureRelationshipIds);

    Map<String, Object> configDetailsMap = CSConfigServer.instance().request(configRequestModel, "GetConfigDetailsForSaveRelationshipInstance", "en_US");
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(configDetailsMap), GetConfigDetailsForSaveRelationshipInstancesResponseModel.class);
    return configDetails;
  }
}