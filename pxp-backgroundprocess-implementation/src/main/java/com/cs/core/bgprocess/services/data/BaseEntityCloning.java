  package com.cs.core.bgprocess.services.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.ListUtils;
import org.json.simple.JSONObject;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityPlanForBulkCloneDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBaseEntityPlanForBulkCloneDTO;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.templating.GetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.coupling.dto.DataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.coupling.idto.IDataTransferForClonedEntitiesDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Cloning of base entities
 *
 * @author vallee
 */
public class BaseEntityCloning extends AbstractBaseEntityProcessing implements IBGProcessJob {
  
  protected final IBaseEntityPlanForBulkCloneDTO bulkCloneDTO          = new BaseEntityPlanForBulkCloneDTO();
  protected Set<String>                          natureRelationshipIds = new HashSet<>();
  RDBMSComponentUtils                            rdbmsComponentUtils;
  
  protected GoldenRecordUtils                    goldenRecordUtils;
  
  private static final String                    SERVICE       = "HANDLE_DATA_TRANSFER_FOR_CLONED_ENTITIES";

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);

    if (!jobData.getRuntimeData().isEmpty()) {
      bulkCloneDTO.fromJSON(jobData.getEntryData().toString());
      rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    }
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws Exception
  {
    List<Long> allClonedEntitiesIIDs = new ArrayList<>();
   
    for (Long baseEntityIID : batchIIDs) {

      userSession.setTransactionId(UUID.randomUUID().toString());
      Map<String, Object> requestModel = new HashMap<>();
      IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(baseEntityIID);
      List<String> klassIds = new ArrayList<>();
      klassIds.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
      requestModel.put("klassIds", klassIds);
      Map<String, Object> configDetails = CSConfigServer.instance().request(requestModel, "GetNumberOfVersionsToMaintain", "en_US");
      IGetNumberOfVersionsToMaintainResponseModel configDetailsForNumberOfVersionAllowed = ObjectMapperUtil
          .readValue(ObjectMapperUtil.writeValueAsString(configDetails), GetNumberOfVersionsToMaintainResponseModel.class);

      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
      IBaseEntityDTO cloneDto = cloneBaseEntity(catalogDao, baseEntityIID);
      IBaseEntityDAO cloneDao = rdbmsComponentUtils.getBaseEntityDAO(cloneDto); 
      
      KlassInstanceUtils.handleDefaultImage(cloneDao);
      
      rdbmsComponentUtils.createNewRevision(cloneDto, configDetailsForNumberOfVersionAllowed.getNumberOfVersionsToMaintain());
      
      allClonedEntitiesIIDs.addAll(catalogDao.getAllChildrenIIDs(cloneDto.getBaseEntityIID()));
      allClonedEntitiesIIDs.add(cloneDto.getBaseEntityIID());

      evaluateRuleHandling(catalogDao, cloneDto, cloneDao, false);
      catalogDao.postUsecaseUpdate(cloneDto.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
      
      goldenRecordUtils.initiateEvaluateGoldenRecordBucket(cloneDto);
    }
    
 // BGP call to handle coupling for all cloned Entities (variants also)
    if(!allClonedEntitiesIIDs.isEmpty()) {
      prepareDataTransferDTOForClonedEntities(rdbmsComponentUtils.getLocaleCatlogDAO(), allClonedEntitiesIIDs);
    }
  }
  
  public void prepareDataTransferDTOForClonedEntities(ILocaleCatalogDAO localeCatlogDAO, List<Long> allClonedEntitiesIIDs) throws Exception
  {
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    
    IDataTransferForClonedEntitiesDTO dataTransferForClonedEntitiesDTO = new DataTransferForClonedEntitiesDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    dataTransferForClonedEntitiesDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    dataTransferForClonedEntitiesDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    dataTransferForClonedEntitiesDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    dataTransferForClonedEntitiesDTO.setUserId(rdbmsComponentUtils.getUserID());
    dataTransferForClonedEntitiesDTO.setClonedBaseEntityIIDs(allClonedEntitiesIIDs);
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE, "", userPriority,
        new JSONContent(dataTransferForClonedEntitiesDTO.toJSON()));
  }
  
  /**
   * Clone a base entity given by IID
   *
   * @param baseEntityIID
   * @throws Exception
   */
  private IBaseEntityDTO cloneBaseEntity(ILocaleCatalogDAO catalogDao, long baseEntityIID)
      throws Exception
  {
    Set<IPropertyDTO> properties = new HashSet<>();
    if (processPlan.getAllProperties()) {
      properties.addAll(catalogDao.getAllEntityProperties(baseEntityIID));
    }
    else {
      Set<Long> propertyIIDs = processPlan.getPropertyIIDs();
      for (Long propertyIID : propertyIIDs)
        properties.add(ConfigurationDAO.instance().getPropertyByIID(propertyIID));
    }
    if (!natureRelationshipIds.isEmpty()) {
      
      properties = removeNatureRelationships(properties);
    }

    List<String> relationshipsToBeCloned = new ArrayList<>();
    for(IPropertyDTO property: properties) {
      if(property.getPropertyType().equals(PropertyType.RELATIONSHIP)){
        relationshipsToBeCloned.add(property.getPropertyCode());
      }
    }

    RDBMSLogger.instance().info("Start cloning Base Entity %d with %d properties", baseEntityIID, properties.size());
    IBaseEntityDTO clonedEntity = catalogDao.cloneEntityByIID( baseEntityIID, new HashSet<>(), properties.toArray(new IPropertyDTO[0]));

    RDBMSLogger.instance().info( "Base Entity %s cloned from base entity IID %d", clonedEntity.toCSExpressID(), baseEntityIID);
    jobData.getLog().info("Base Entity %s cloned from base entity IID %d", clonedEntity.toCSExpressID(), baseEntityIID);
    return clonedEntity;
  }
  
  private Set<IPropertyDTO> removeNatureRelationships(
      Set<IPropertyDTO> propertiesToClone)
  {
    Set<IPropertyDTO> updatedProperties = new HashSet<IPropertyDTO>();
    IPropertyDTO[] properties = propertiesToClone.toArray(new IPropertyDTO[0]);
    for (IPropertyDTO property : properties) {
      if (!natureRelationshipIds.contains(property.getCode())) {
        
        updatedProperties.add(property);
      }
    }
    return updatedProperties;
  }

  private void evaluateRuleHandling(ILocaleCatalogDAO catalogDao, IBaseEntityDTO cloneDto, IBaseEntityDAO cloneDao,
      Boolean isClassifierModified) throws Exception
  {
    RuleHandler ruleHandler = new RuleHandler();
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    List<String> removedClassifiers = new ArrayList<>();
    Boolean isLocaleIdsApplicable = isClassifierModified ? isClassifierModified : ruleHandler.evaluateLocaleIdsApplicable(cloneDto);
    ruleHandler.getBaseEntityClassifiersAndTaxonomies(catalogDao, cloneDao, baseEntityClassifiers, baseEntityTaxonomies);
    
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
    requestModel.put("removedClassifiers", removedClassifiers);
    JSONObject detailsFromODB = CSConfigServer.instance().request(requestModel, "GetProductIdentifiersForClassifiers",
        catalogDao.getLocaleCatalogDTO().getLocaleID());
    IDataRulesHelperModel dataRulesHelperModel = ObjectMapperUtil.readValue(ObjectMapperUtil.writeValueAsString(detailsFromODB),
        DataRulesHelperModel.class);
    
    ruleHandler.applyDQRules(catalogDao, cloneDto.getBaseEntityIID(), baseEntityClassifiers, baseEntityTaxonomies, removedClassifiers,
        isLocaleIdsApplicable, RuleType.dataquality, dataRulesHelperModel.getReferencedElements(),
        dataRulesHelperModel.getReferencedAttributes(), dataRulesHelperModel.getReferencedTags());
    
    ruleHandler.evaluateDQMustShould(cloneDto.getBaseEntityIID(), dataRulesHelperModel, cloneDao, catalogDao);
    
  }
}
