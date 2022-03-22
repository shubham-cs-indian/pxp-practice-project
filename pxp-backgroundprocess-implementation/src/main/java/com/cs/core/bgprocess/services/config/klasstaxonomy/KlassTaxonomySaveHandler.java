package com.cs.core.bgprocess.services.config.klasstaxonomy;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.KlassTaxonomySaveDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IKlassTaxonomySaveDTO;
import com.cs.core.bgprocess.idto.IRemoveAttributeVariantContextsDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.services.datatransfer.ClassificationDataTransferOnConfigChange;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.config.strategy.usecase.concatenatedAttribute.IGetConfigDetailsForSaveConcatenatedAttributeStrategy;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAS;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import org.apache.commons.collections4.ListUtils;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

public class KlassTaxonomySaveHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  IKlassTaxonomySaveDTO klassTaxonomySaveDTO = new KlassTaxonomySaveDTO();
  WorkflowUtils workflowUtils;

  private String[]      stepLabels           = { "MandatoryViolation", "ProductIdentifier",
      "RemoveEmbedded", "DeleteNatureRelationship", "RemoveTaxonomyConflicts" ,"DefaultValueWithCouplingType", "RemoveAttributeVariants", "CreateCalculatedAttributeInstance"};
  

  
  private enum STEPS
  {
    
    MANDATORY_VIOLATION, PRODUCT_IDENTIFIER, REMOVE_EMBEDDED, DELETE_NATURE_RELATIONSHIP, REMOVE_TAXONOMY_CONFLICTS, DEFAULT_VALUE_WITH_COUPLING_TYPE, REMOVE_ATTRIBUTE_VARIANTS, CREATE_CALCULATED_ATTRIBUTE_INSTANCE;
    
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
    klassTaxonomySaveDTO.fromJSON(jobData.getEntryData().toString());
    workflowUtils = BGProcessApplication.getApplicationContext().getBean(WorkflowUtils.class);
    this.initProgressData();
    
    RDBMSLogger.instance()
        .debug("End of initialization BGP %s / iid=%d", getService(), getIID());
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
    int currentStepNo = jobData.getProgress()
        .getCurrentStepNo();
    STEPS currentStep = STEPS.valueOf(currentStepNo - 1);
    
    switch (currentStep) {
      case MANDATORY_VIOLATION:
        handleMandatoryViolation();
        break;
      case PRODUCT_IDENTIFIER:
        handleProductIdentifier();
        break;
      case REMOVE_EMBEDDED:
        handleRemoveEmbedded();
        break;
      case DELETE_NATURE_RELATIONSHIP:
        handleDeleteNatureRelationship();
        break;
      case REMOVE_TAXONOMY_CONFLICTS:
        removeTaxonomyConflicts();
        break;
      
      case DEFAULT_VALUE_WITH_COUPLING_TYPE:
        ClassificationDataTransferOnConfigChange.executeForClassificationCoupling(rdbmsComponentUtils,
            klassTaxonomySaveDTO.getModifiedCoupledPropertyDTOs());
      case REMOVE_ATTRIBUTE_VARIANTS:
        removeAttributeVariants();
        break;
      case CREATE_CALCULATED_ATTRIBUTE_INSTANCE:
        createCalculatedAttributeInstance();
        break;
      default:
        throw new RDBMSException(100, "Programm Error", "Unexpected step-no: " + currentStepNo);
    }
    
    jobData.getProgress().incrStepNo();
    
    // Return of status
    IBGProcessDTO.BGPStatus status = null;
    if (jobData.getProgress().getPercentageCompletion() == 100)
      status = jobData.getSummary()
          .getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
              : IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    else
      status = BGPStatus.RUNNING;
    
    return status;
  }
  

  private static final String GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID = "Select baseentityiid from pxp.baseentity where classifieriid in (%s) and ismerged != true "
      + "UNION ALL "
      + "Select becl.baseentityiid from pxp.baseentityclassifierlink becl join pxp.baseentity be on becl.baseentityiid = be.baseentityiid and be.ismerged != true where becl.otherclassifieriid in (%s)";
  
  private Set<Long> getBaseEntityIIDs(Set<Long> classifierIIDs) throws RDBMSException
  {
    Set<Long> baseEntityIIDs = new HashSet<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String join = Text.join(",", classifierIIDs);
          String baseEntityIIDsQuery = String.format(GET_BASE_ENITY_IIDS_FROM_CLASSIFIER_IID, join,
              join);
          PreparedStatement stmt = currentConn.prepareStatement(baseEntityIIDsQuery);
          stmt.execute();
          IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
          while (result.next()) {
            baseEntityIIDs.add(result.getLong("baseentityiid"));
          }
        });
    return baseEntityIIDs;
  }
  
  private Set<Long> getClassifierIIDs(Set<String> classifierCodes) throws RDBMSException
  {
    
    Set<Long> classifierIIDs = new HashSet<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String classifierQuery = String.format(
              "Select classifieriid from pxp.classifierconfig where classifiercode in (%s)",
              Text.join(",", classifierCodes, "'%s'"));
          PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
          stmt.execute();
          IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
          while (result.next()) {
            classifierIIDs.add(result.getLong("classifieriid"));
          }
        });
    return classifierIIDs;
    
  }
  
  private void handleDeleteNatureRelationship() throws Exception
  {
    Set<Long> baseEntityIIDs = new HashSet<>();
    List<Long> deleteNatureRelationshipPropertyIIDs = klassTaxonomySaveDTO.getDeletedNatureRelationshipPropertyIIDs();
    if (deleteNatureRelationshipPropertyIIDs.isEmpty()) {
      return;
    }
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String classifierQuery = String.format("Select side1entityiid from pxp.relation where propertyiid in (%s)",
          Text.join(",", deleteNatureRelationshipPropertyIIDs));
      PreparedStatement stmt = currentConn.prepareStatement(classifierQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        baseEntityIIDs.add(result.getLong("side1entityiid"));
      }
    });
    
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    for (Long propertyIID : deleteNatureRelationshipPropertyIIDs) {
      IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
      propertyDTO.setRelationSide(IPropertyDTO.RelationSide.valueOf(1));
      propertyDTOs.add(propertyDTO);
    }
    
    for (long baseEntityIID : baseEntityIIDs) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(propertyDTOs.toArray(new IPropertyDTO[0]));
      Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
      Set<IPropertyRecordDTO> updatedPropertyRecords = new HashSet<IPropertyRecordDTO>();
      for(IPropertyRecordDTO propertyRecord: propertyRecords) {
        RelationsSetDTO relationSetDTO = (RelationsSetDTO) propertyRecord;
        relationSetDTO.removeRelations(relationSetDTO.getSideBaseEntityIIDs());
        relationSetDTO.setChanged(true);
        updatedPropertyRecords.add(relationSetDTO);
      }
      if (!updatedPropertyRecords.isEmpty()) {
        baseEntityDAO.updatePropertyRecords(updatedPropertyRecords.toArray(new IPropertyRecordDTO[0]));
        //rdbmsComponentUtils.createNewRevision(baseEntityDAO.getBaseEntityDTO());
      }
      
    }
    
    /*RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.getProcedure("sp_removerelationproperty")
          .setInput(ParameterType.IID_ARRAY, deleteNatureRelationshipPropertyIIDs)
          .execute();
    });*/
  }
  
  private void handleProductIdentifier() throws RDBMSException, CSFormatException
  {
    List<Long> propertyIIDsToEvaluateProductIdentifier = klassTaxonomySaveDTO.getPropertyIIDsToEvaluateProductIdentifier();
    List<Long> propertyIIDsToRemoveProductIdentifier = klassTaxonomySaveDTO.getPropertyIIDsToRemoveProductIdentifier();
    if (propertyIIDsToEvaluateProductIdentifier.isEmpty()
        && propertyIIDsToRemoveProductIdentifier.isEmpty()) 
    {
      return;
    }
    Set<Long> classifierIIDs = getClassifierIIDs(klassTaxonomySaveDTO.getClassifierCodes());
    Set<Long> baseEntityIIDs = getBaseEntityIIDs(classifierIIDs);
    if (!propertyIIDsToEvaluateProductIdentifier.isEmpty()) {
      for (Long baseEntityIID : baseEntityIIDs) {
        evaluateProductIdentifiers(baseEntityIID, propertyIIDsToEvaluateProductIdentifier);
      }
    }
    
    if (!propertyIIDsToRemoveProductIdentifier.isEmpty()) {
      removeProductIdentifiers(propertyIIDsToRemoveProductIdentifier, baseEntityIIDs);
    }
  }
  
  private void removeProductIdentifiers(List<Long> propertyIIDs, Set<Long> entityIIDs) throws RDBMSException
  {
    Set<Long> classifierIIDs = getClassifierIIDs(klassTaxonomySaveDTO.getClassifierCodes());
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String query = "delete from pxp.uniquenessviolation where ( sourceiid IN ("
              + Text.join(",", entityIIDs) + ") OR targetiid IN (" + Text.join(",", entityIIDs)
              + ") ) AND propertyiid IN (" + Text.join(",", propertyIIDs)
              + ") AND classifieriid IN (" + Text.join(",", classifierIIDs) + ")";
          PreparedStatement stmt = currentConn.prepareStatement(query);
          stmt.execute();
        });
  }
  
  @SuppressWarnings("unchecked")
  private void evaluateProductIdentifiers(long baseEntityIID, List<Long> propertyIIDs
      ) throws RDBMSException, CSFormatException
  {
    ILocaleCatalogDAO localCatalogDAO = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
    IBaseEntityDTO entity = localCatalogDAO.getEntityByIID(baseEntityIID);
    BaseEntityDAO entityDao = (BaseEntityDAO) localCatalogDAO.openBaseEntity(entity);
    IUniquenessViolationDAO uniquenessDAO = localCatalogDAO.openUniquenessDAO();
    List<IPropertyDTO> propertyDTO = new ArrayList<>();
    List<String> baseEntityClassifierCodes = getBaseEntityClassifierCodes(entityDao);
    Set<String> classifierCodes = klassTaxonomySaveDTO.getClassifierCodes();
    List<String> classifierCodesInList = new ArrayList<String>(classifierCodes);
    
    Set<String> classifierCodesToEvaluate = new HashSet<String>(
        ListUtils.intersection(classifierCodesInList, baseEntityClassifierCodes));
    Set<Long> classifierIIDsToEvaluate = getClassifierIIDs(classifierCodesToEvaluate);
    
    for (Long propertyIID : propertyIIDs) {
      propertyDTO.add(ConfigurationDAO.instance()
          .getPropertyByIID(propertyIID));
    }
    
    IBaseEntityDTO propertyRecords = entityDao
        .loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[] {}));
    
    for (IPropertyRecordDTO propertyRecord : propertyRecords.getPropertyRecords()) {
      
      long identifierIID = propertyRecord.getProperty().getPropertyIID();
      
      String catalogCode = entityDao.getBaseEntityDTO()
          .getCatalog()
          .getCatalogCode();
      /**
       * ClassifierIIDs will be more than one for same entity in case of NN class or  taxonomy is added in entity.
       * and from config same class is inherited by nature class and vice versa.
       */
      for (Long classifierIID : classifierIIDsToEvaluate) {
        List<Long> violatedEntities = uniquenessDAO.evaluateProductIdentifier(identifierIID,
            baseEntityIID, classifierIID, catalogCode);
        
        if (violatedEntities.size() > 1) {
          violatedEntities.remove(baseEntityIID);
          List<IUniquenessViolationDTO> toInsertViolatedEntities = new ArrayList<>();
          
          for (long targetEntityIID : violatedEntities) {
            IUniquenessViolationDTO uniquenessViolationDTO = localCatalogDAO
                .newUniquenessViolationBuilder(baseEntityIID, targetEntityIID, identifierIID,
                    classifierIID)
                .build();
            
            toInsertViolatedEntities.add(uniquenessViolationDTO);
            
          }
          uniquenessDAO.insertViolatedEntity(toInsertViolatedEntities);
        }
      }
    }
  }
  
  private List<String> getBaseEntityClassifierCodes(BaseEntityDAO entityDao) throws RDBMSException
  {
    List<IClassifierDTO> classifiers = entityDao.getClassifiers();
    List<String> classifierCodes = new ArrayList<String>();
    
    for (IClassifierDTO classifier : classifiers) {
        classifierCodes.add(classifier.getClassifierCode());
    }
    classifierCodes.add(entityDao.getBaseEntityDTO()
        .getNatureClassifier()
        .getCode());
    
    return classifierCodes;
  }
  
  private void handleRemoveEmbedded() throws RDBMSException 
  {
    List<Long> removedEmbeddedClassifierIIDs = klassTaxonomySaveDTO.getRemovedEmbeddedClassifierIIDs();
    if (removedEmbeddedClassifierIIDs.isEmpty()) {
      return;
    }
    Long savedKlassTaxonomyClassifierIID = klassTaxonomySaveDTO.getSavedKlassTaxonomyClassifierIID();
    Set<Long> classifierIIDs = new HashSet<>();
    classifierIIDs.add(savedKlassTaxonomyClassifierIID);
    Set<Long> baseEntityIIDs = getBaseEntityIIDs(classifierIIDs);
    for (Long baseEntityIID : baseEntityIIDs) {
      try {
        deleteEmbeddedInstances(removedEmbeddedClassifierIIDs, baseEntityIID);
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
  }
  
  private final String QUERY_TO_GET_CONTEXT_ENTITY_IDS = "select baseentityiid from pxp.baseentity where parentiid = (%s) AND classifieriid IN (%s) and ismerged != true;";
  
  @SuppressWarnings("unchecked")
  private void deleteEmbeddedInstances(List<Long> removedContextClassifierIIDs, Long productIID) throws Exception {
    List<Long> baseEntityIIDsToRemove = new ArrayList<Long>();
    RuleHandler ruleHandler = new RuleHandler();
    
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, productIID);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(productIID);
    BaseEntityDAO parentEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    
    ruleHandler.getBaseEntityClassifiersAndTaxonomies(catalogDao, parentEntityDAO, baseEntityClassifiers, baseEntityTaxonomies);
    
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("klassCodes", baseEntityClassifiers);
    requestModel.put("taxonomyCodes", baseEntityTaxonomies);
    requestModel.put("removedContextClassifierIIDs", removedContextClassifierIIDs);
    JSONObject detailsFromODB = CSConfigServer.instance()
        .request(requestModel, "GetConfigDetailsForRuntimeCleanupOnKlassOrTaxonomySave",
            catalogDao.getLocaleCatalogDTO()
                .getLocaleID());
    List<Long> classifierIIDsToRemove = (List<Long>) detailsFromODB.get("classifierIIDsToRemove");
    
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      String classifieriid = Text.join(",", classifierIIDsToRemove);
      String finalQuery = String.format(QUERY_TO_GET_CONTEXT_ENTITY_IDS, productIID, classifieriid);
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        baseEntityIIDsToRemove.add(result.getLong("baseentityiid"));
      }
    });
    
    if (!baseEntityIIDsToRemove.isEmpty()) {
      RDBMSComponentUtils rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
      try {
        BgprocessUtils.deleteChildrens(baseEntityIIDsToRemove, rdbmsComponentUtils.getUserID(), catalogDao);
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
      for (Long baseEntityIID : baseEntityIIDsToRemove) {
        BgprocessUtils.deleteBaseEntity(baseEntityIID, parentEntityDAO, userSession, null);
      } 
    }
  }
  
  private void removeAttributeVariants() throws Exception
  {
    List<IRemoveAttributeVariantContextsDTO> removeAttributeVariantContextsDTO = klassTaxonomySaveDTO.getRemoveAttributeVariantContextsDTO();
    
    if (removeAttributeVariantContextsDTO.isEmpty())
      return;
    
    Set<Long> removedClassifiersForAttributeContext = new HashSet<Long>(klassTaxonomySaveDTO.getChangedClassifiersForAttributeContexts());
    Set<Long> baseEntityIIDs = getBaseEntityIIDs(removedClassifiersForAttributeContext);
    for (Long baseEntityIID : baseEntityIIDs) {
        handleRemoveAttributeVariantsPerContent(removeAttributeVariantContextsDTO, baseEntityIID);
    }
  }

  private void handleRemoveAttributeVariantsPerContent(List<IRemoveAttributeVariantContextsDTO> removeAttributeVariantContextsDTO, Long baseEntityIID) throws Exception
  {
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
    IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    
    new RuleHandler().getBaseEntityClassifiersAndTaxonomies(catalogDao, baseEntityDAO, baseEntityClassifiers, baseEntityTaxonomies);
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("klassCodes", baseEntityClassifiers);
    requestModel.put("taxonomyCodes", baseEntityTaxonomies);
    requestModel.put("removedAttributeIdVsContextIds", removeAttributeVariantContextsDTO.stream().collect(Collectors.toMap(dto ->
        dto.getAttributeId(), dto -> dto.getAttributeContextsIds())));
    JSONObject detailsFromODB = CSConfigServer.instance()
        .request(requestModel, "GetConfigDetailsForRuntimeCleanupOnKlassOrTaxonomySave",
            catalogDao.getLocaleCatalogDTO()
                .getLocaleID());
    Map<String, List<Long>> notApplicableAttributeIdVsContextIds = (Map<String, List<Long>>) detailsFromODB.get("removedAttributeIdVsContextIds");
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currConnection) -> {
      List<IValueRecordDTO> deletedValueRecords = new ArrayList<>();;
      BaseEntityDAS entityDAS = new BaseEntityDAS(currConnection);
      for(Map.Entry<String, List<Long>> entry: notApplicableAttributeIdVsContextIds.entrySet()) {
        try {
          deletedValueRecords = entityDAS.getAllAttributeVariantsValueIIDSByPropertyIID( entry.getKey(), entry.getValue(), baseEntityDAO);
        }
        catch (Exception e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
        }
       }
      if(!deletedValueRecords.isEmpty()) {
        baseEntityDAO.deletePropertyRecords(deletedValueRecords.toArray(new IValueRecordDTO[deletedValueRecords.size()]));
      }
    });
  }

  private void handleMandatoryViolation()
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    List<Long> updatedMandatoryPropertyIIDs = klassTaxonomySaveDTO
        .getUpdatedMandatoryPropertyIIDs();
    if (updatedMandatoryPropertyIIDs.isEmpty()) {
      return;
    }
    
    Set<Long> classifierIIDs = getClassifierIIDs(klassTaxonomySaveDTO.getClassifierCodes());
    Set<Long> baseEntityIIDs = getBaseEntityIIDs(classifierIIDs);
    for (Long baseEntityIID : baseEntityIIDs) {
      evaluateMandatoryViolations(baseEntityIID);
    }
  }
  
  @SuppressWarnings("unchecked")
  protected void evaluateMandatoryViolations(Long baseEntityIID)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
    RuleHandler ruleHandler = new RuleHandler();
    
    IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
    BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    List<String> removedClassifiers = new ArrayList<>();
    
    ruleHandler.getBaseEntityClassifiersAndTaxonomies(catalogDao, entityDao, baseEntityClassifiers,
        baseEntityTaxonomies);
    
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
    requestModel.put("removedClassifiers", removedClassifiers);
    
    JSONObject detailsFromODB = CSConfigServer.instance()
        .request(requestModel, "GetProductIdentifiersForClassifiers",
            catalogDao.getLocaleCatalogDTO()
                .getLocaleID());
    List<Long> shouldPropertyIds = new ArrayList<Long>();
    
    List<Long> updatedMandatoryPropertyIIDs = new ArrayList<Long>(
        klassTaxonomySaveDTO.getUpdatedMandatoryPropertyIIDs());
    
    List<Long> consolidatedMandatoryPropertyIIDs = getConsolidatedMandatoryPropertyIIDs(
        detailsFromODB, updatedMandatoryPropertyIIDs, shouldPropertyIds);
    // Add removedClassifiers from orientDB response to nonEmptyPropertyIds.
    List<Long> nonEmptyPropertyIds = new ArrayList<Long>();
    List<IPropertyDTO> propertyDTO = new ArrayList<>();
    for (Long mandatoryPropertyIID : consolidatedMandatoryPropertyIIDs) {
      propertyDTO.add(ConfigurationDAO.instance()
          .getPropertyByIID(mandatoryPropertyIID));
    }
    
    IBaseEntityDTO propertyRecords = entityDao
        .loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[] {}));
    
    for (IPropertyRecordDTO propertyRecord : propertyRecords.getPropertyRecords()) {
      long propertyIID = propertyRecord.getProperty()
          .getPropertyIID();
      if (propertyRecord instanceof ValueRecordDTO) {
        if (((IValueRecordDTO) propertyRecord).getValue()
            .isEmpty()) {
          continue;
        }
      }
      else {
        if (((TagsRecordDTO) propertyRecord).getTagValueCodes()
            .isEmpty()) {
          continue;
        }
      }
      nonEmptyPropertyIds.add(propertyIID);
    }
    consolidatedMandatoryPropertyIIDs.removeAll(nonEmptyPropertyIds);
    ruleHandler.fillEmptyMandatoryViolations(baseEntityIID, consolidatedMandatoryPropertyIIDs,
        shouldPropertyIds,(List<Long>)detailsFromODB.get(IDataRulesHelperModel.TRANSLATABLE_PROPERTY_IIDS), catalogDao);
    updatedMandatoryPropertyIIDs.removeAll(consolidatedMandatoryPropertyIIDs);
    List<Long> nonMandatoryPropertyIIDs = ListUtils.union(nonEmptyPropertyIds,
        updatedMandatoryPropertyIIDs);
    
    ruleHandler.removeMandatoryViolations(baseEntityIID, nonMandatoryPropertyIIDs, catalogDao);
  }
  
  @SuppressWarnings("unchecked")
  public List<Long> getConsolidatedMandatoryPropertyIIDs(JSONObject detailsFromODB,
      List<Long> updatedMandatoryPropertyIIDs, List<Long> shouldPropertyIDs)
  {
    List<Long> mustPropertyIds = (List<Long>) detailsFromODB.get(IDataRulesHelperModel.MUST);
    List<Long> shouldPropertyIds = (List<Long>) detailsFromODB.get(IDataRulesHelperModel.SHOULD);
    
    mustPropertyIds.retainAll(updatedMandatoryPropertyIIDs);
    shouldPropertyIds.retainAll(updatedMandatoryPropertyIIDs);
    List<Long> mandatoryPropertyIds = ListUtils.sum(mustPropertyIds, shouldPropertyIds);
    shouldPropertyIDs.clear();
    shouldPropertyIDs.addAll(shouldPropertyIds);
    return mandatoryPropertyIds;
  }
  
  private void removeTaxonomyConflicts() throws RDBMSException
  {
    if(!klassTaxonomySaveDTO.getModifiedRelationshipIIDsForTaxonomyInheritance().isEmpty()) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        String removeTaxonomyConflictsQuery = String.format("Delete from pxp.baseentitytaxonomyconflictlink where propertyiid in (%s)",Text.join(",", klassTaxonomySaveDTO.getModifiedRelationshipIIDsForTaxonomyInheritance()));
        PreparedStatement stmt = currentConn.prepareStatement(removeTaxonomyConflictsQuery);
        stmt.execute();
      });
    }
    
  }
  
  private void createCalculatedAttributeInstance() throws Exception
  {
    List<String> addedCalculatedAttributeIds = klassTaxonomySaveDTO.getAddedCalculatedAttributeIds();
    if (addedCalculatedAttributeIds.isEmpty()) {
      return;
    }
    
    Set<Long> classifierIIDs = getClassifierIIDs(klassTaxonomySaveDTO.getClassifierCodes());
    Set<Long> baseEntityIIDs = getBaseEntityIIDs(classifierIIDs);
    
    for (Long baseEntityIID : baseEntityIIDs) {
      
      ILocaleCatalogDAO localCatalogDAO = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
      IBaseEntityDTO baseEntityDTO = localCatalogDAO.getEntityByIID(baseEntityIID);
      BaseEntityDAO baseEntityDAO = (BaseEntityDAO) localCatalogDAO.openBaseEntity(baseEntityDTO);
      
      // configDetails for each content for calculation formula
      IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetails(baseEntityDTO);
      
      Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
      Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
      
      PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, referencedAttributes,
          configDetails.getReferencedTags(), referencedElements, PropertyRecordType.DEFAULT, localCatalogDAO);
      
      List<IPropertyRecordDTO> addedPropertyRecords = new ArrayList<IPropertyRecordDTO>();
      List<String> changedAttributeIds = new ArrayList<>();
      for (String calculatedAttributeId : addedCalculatedAttributeIds) {
        
        IAttribute referencedAttribute = referencedAttributes.get(calculatedAttributeId);
        IReferencedSectionAttributeModel referencedElement = (IReferencedSectionAttributeModel) referencedElements.get(calculatedAttributeId);

        if(referencedElement == null) {
          continue;
        }
        
        IPropertyRecordDTO propertyRecordDTO = propertyRecordBuilder.createCalculatedAttribute(referencedElement, referencedAttribute, null,
            null, PropertyType.CALCULATED);
        
        addedPropertyRecords.add(propertyRecordDTO);
        changedAttributeIds.add(calculatedAttributeId);
      }
      
      baseEntityDAO.createPropertyRecords(addedPropertyRecords.toArray(new IPropertyRecordDTO[addedPropertyRecords.size()]));
      if(!changedAttributeIds.isEmpty()) {
        localCatalogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      }
      IBusinessProcessTriggerModel businessProcessEventModel = BgprocessUtils.getBusinessProcessModelForPropetiesSave(baseEntityDAO, changedAttributeIds, new ArrayList<>());
      if(businessProcessEventModel != null) {
        workflowUtils.executeBusinessProcessEvent(businessProcessEventModel);
      }
    }
  }
  
  private IConfigDetailsForSaveConcatenatedAttributeModel getConfigDetails(IBaseEntityDTO baseEntity) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    
    List<String> nonNatureClassifierCodes = new ArrayList<>();
    nonNatureClassifierCodes.add(baseEntity.getNatureClassifier().getCode());
    List<String> selectedTaxonomyCodes = new ArrayList<>();
    baseEntity.getOtherClassifiers().forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        nonNatureClassifierCodes.add(classifier.getCode());
      }
      else {
        selectedTaxonomyCodes.add(classifier.getCode());
      }
    });
    
    multiclassificationRequestModel.setKlassIds(nonNatureClassifierCodes);
    multiclassificationRequestModel.setSelectedTaxonomyIds(selectedTaxonomyCodes);
    
    IGetConfigDetailsForSaveConcatenatedAttributeStrategy getConfigDetailsForSaveConcatenatedAttributeStrategy = BGProcessApplication
        .getApplicationContext().getBean(IGetConfigDetailsForSaveConcatenatedAttributeStrategy.class);
    
    IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetailsForSaveConcatenatedAttributeStrategy
        .execute(multiclassificationRequestModel);
    
    return configDetails;
  }
}
