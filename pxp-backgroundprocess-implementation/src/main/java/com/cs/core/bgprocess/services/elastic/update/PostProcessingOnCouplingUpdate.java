package com.cs.core.bgprocess.services.elastic.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.services.datatransfer.DataTransferUtil;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO.Change;
import com.cs.core.transactionend.handlers.dto.IPostProcessingOnCouplingUpdateDTO;
import com.cs.core.transactionend.handlers.dto.PostProcessingOnCouplingUpdateDTO;

public class PostProcessingOnCouplingUpdate extends AbstractBGProcessJob implements IBGProcessJob {
  
  LocaleCatalogDAO                   localeCatalogDAO;
  IPostProcessingOnCouplingUpdateDTO dependencyDTO  = new PostProcessingOnCouplingUpdateDTO();
  static RDBMSComponentUtils         rdbmsComponentUtils;
  protected GoldenRecordUtils        goldenRecordUtils;
  private int                        currentBatchNo = 0;
  private int                        nbBatches      = 1;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    dependencyDTO.fromJSON(jobData.getEntryData().toString());
    IUserSessionDAO userSessionDAO = openUserSession();
    localeCatalogDAO = (LocaleCatalogDAO) userSessionDAO.openLocaleCatalog(userSession,
        new LocaleCatalogDTO(dependencyDTO.getLocaleID(), dependencyDTO.getCatalogCode(), dependencyDTO.getOrganizationCode()));
    
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    postProcessEntity();
    setCurrentBatchNo(++currentBatchNo);
    
    IBGProcessDTO.BGPStatus status = null;
    
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    return status;
  }

  private void postProcessEntity() throws RDBMSException, Exception, CSFormatException
  {
    Long targetEntityIID = dependencyDTO.getChanges().getEntityIID();
    
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDTO entity = localeCatalogDAO.getEntityByIID(targetEntityIID);
    if (!entity.getCatalog().getCatalogCode().contains(CommonConstants.DEFAULT_KLASS_INSTANCE_CATALOG)) {
      IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(entity);
      
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      
      List<IPropertyDTO> propertyDTOs = new ArrayList<>();
      
      IConfigDetailsForBulkPropagationResponseModel configDetails = DataTransferUtil.getConfigDetailForBaseEntity(baseEntityDAO);
      RuleHandler ruleHandler = new RuleHandler();
      IChangedPropertiesDTO changePropertiesDTO = ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(),
          true, configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
      
      IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
      IObjectRevisionDTO lastObjectRevision = revisionDAO.getLastObjectRevision(targetEntityIID);
      IBaseEntityDTO baseEntityDTOArchive = lastObjectRevision.getBaseEntityDTOArchive();
      
      Map<String, Change> propertiesChange = dependencyDTO.getChanges().getPropertiesChange();
      for (Entry<String, Change> propertiesChangeEntryMap : propertiesChange.entrySet()) {
        if (propertiesChangeEntryMap.getValue().equals(Change.Deleted)) {
          continue;
        }
        else {
          IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(propertiesChangeEntryMap.getKey());
          propertyDTOs.add(propertyByCode);
        }
      }
      baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTOs.toArray(new PropertyDTO[0]));
      
      Map<Long, Set<Long>> findTargets = baseEntityDAO.findTargets(baseEntityDTO.getPropertyRecords());
      Boolean isCalculatedUpdated = false;
      Set<Long> calculatedPropertyIIDs = findTargets.get(targetEntityIID);
      if(calculatedPropertyIIDs !=null && !calculatedPropertyIIDs.isEmpty()) {
        Map<Long, IPropertyDTO> propertyMap = new HashMap<>();
        
        IPropertyDTO[] properties = new IPropertyDTO[calculatedPropertyIIDs.size()];
        int i = 0;
        for (Long propertyIID : calculatedPropertyIIDs) {
          if (!propertyMap.containsKey(propertyIID)) {
            propertyMap.put(propertyIID, ConfigurationDAO.instance().getPropertyByIID(propertyIID));
          }
          properties[i++] = propertyMap.get(propertyIID);
        }
        baseEntityDAO.loadPropertyRecords(properties); // Load the target
        // records
        Set<IPropertyRecordDTO> targetRecords = new TreeSet<>();
        targetRecords.addAll(baseEntityDTO.getPropertyRecords());
        for (IPropertyRecordDTO targetRecord : targetRecords) {
          IPropertyRecordDTO propertyRecordArchive = baseEntityDTOArchive.getPropertyRecord(targetRecord.getProperty().getPropertyIID());
          Boolean isUpdated = baseEntityDAO.updateCalculatedPropertyRecords(localeCatalogDAO, targetRecord, propertyRecordArchive);
          if (isUpdated) {
            isCalculatedUpdated = true;
          }
        }
      }
      
      goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDTO);
      
      if (isCalculatedUpdated || !(changePropertiesDTO.getAttributeCodes().isEmpty() && changePropertiesDTO.getTagCodes().isEmpty())) {
        rdbmsComponentUtils.createNewRevision(baseEntityDTO, configDetails.getNumberOfVersionsToMaintain());
        rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(targetEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      }
    }
  }
}
