package com.cs.core.rdbms.services.resolvers;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.dao.AbstractEventHandler;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.*;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dao.RuleCatalogDAO;
import com.cs.core.rdbms.process.dao.RuleCatalogDAS;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Handler for triggering respective rules according to change in entity.
 *
 * @author Niraj.Dighe
 */

public class RuleHandler extends AbstractEventHandler {
  
  private List<IPropertyRecordDTO> records         = new ArrayList<>();
  
  private static final String      SERVICE             = "UNIQUENESS_VIOLATION";

  private static final String      TYPE_SWITCH_SERVICE = "BASE_ENTITY_TYPE_SWITCH";

  /**
   * Build a new rule handler by assigning a number of identification
   *
   */
  public RuleHandler() throws RDBMSException
  {
    super(RDBMSAppDriverManager.getDriver());
    RDBMSLogger.instance().info(" initialized to manage rule events");
  }

  /**
   *
   * The source of rule changes corresponds to an array of property dtos in the
   * JSON part
   * @param timelineDTO
   * @param catalogDao
   * @throws CSFormatException
   */
  private void fillCreatedAndUpdatedPropertyRecords(TimelineDTO timelineDTO, ILocaleCatalogDAO catalogDao)
      throws CSFormatException
  {
    TimelineDTO timelineData = currentEvent.getTimelineData();
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    baseEntity.fromPXON(currentEvent.getJSONExtract()
        .toString());
    
    ICSEList createdRecordsElements = timelineData.getElements(ChangeCategory.CreatedRecord);
    fillPropertyRecordsFromCSE(createdRecordsElements, catalogDao);
    
    ICSEList updatedRecordsElements = timelineData.getElements(ChangeCategory.UpdatedRecord);
    fillPropertyRecordsFromCSE(updatedRecordsElements, catalogDao);
  }
  
  private void fillPropertyRecordsFromCSE(ICSEList recordsElements, ILocaleCatalogDAO catalogDao) throws CSFormatException
  {
    if (recordsElements != null) {
      for (ICSEElement element : recordsElements.getSubElements()) {
        if (element.getSpecification(Keyword.$type).equals(SuperType.TAGS.name())) {
          TagsRecordDTO tagsRecord = new TagsRecordDTO();
          tagsRecord.fromCSExpressID(element);
          records.add(tagsRecord);
        }
        else {
          ValueRecordDTO valueRecord = new ValueRecordDTO();
          valueRecord.fromCSExpressID(element);
          records.add(valueRecord);
          String localeID = valueRecord.getLocaleID();
          if(!StringUtils.isEmpty(localeID)) {
          catalogDao.getLocaleCatalogDTO().setLocaleID(localeID);
          catalogDao.getLocaleCatalogDTO().setLocaleInheritanceSchema(Arrays.asList(localeID));
          }
        }
      }
    }
  }
  
  @Override
  public void run_New()
  {
    try {
      RDBMSLogger.instance()
          .info(" received Event: " + currentEvent);
      
      TimelineDTO sourceChange = currentEvent.getTimelineData();
      String localeID = currentEvent.getLocaleID();
      /*List<String> inheritanceSchema = sourceChange.getInheritanceSchema();
      String localeID = inheritanceSchema.isEmpty() ? IStandardConfig.GLOBAL_LOCALE
          : inheritanceSchema.get(inheritanceSchema.size() - 1);*/
      
      ILocaleCatalogDAO catalogDao = openCatalog(localeID);
    
      fillCreatedAndUpdatedPropertyRecords(currentEvent.getTimelineData(), catalogDao);
      long baseEntityIID = currentEvent.getObjectIID();
      IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
      BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
      List<String> baseEntityClassifiers = new ArrayList<>();
      List<String> baseEntityTaxonomies = new ArrayList<>();
      List<String> removedBaseEntityClassifiers = new ArrayList<>();

      getBaseEntityClassifiersAndTaxonomies(catalogDao, entityDao, baseEntityClassifiers, baseEntityTaxonomies);
      
      TimelineDTO timelineData = currentEvent.getTimelineData();
      ICSEList elements = timelineData.getElements(ChangeCategory.RemovedClassifier);
      ICSEList addedClassifier = timelineData.getElements(ChangeCategory.AddedClassifier);
      

      if(elements != null) {
        
        List<ICSEElement> subElements = elements.getSubElements();
        
        for(ICSEElement element: subElements) {
          String type = element.getSpecification(Keyword.$type);
          if (type.equals(ClassifierType.CLASS.name()) || type.equals(ClassifierType.TAXONOMY.name()) 
              || type.equals(ClassifierType.MINOR_TAXONOMY)) {
            IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(Long.parseLong(element.getSpecification(Keyword.$iid)));
            removedBaseEntityClassifiers.add(classifierDTO.getCode());
          }
        }
      }
      
      Boolean isAllLocaleIdsApplicable = false;
      
      if(addedClassifier == null && elements == null) {
        
        for(IPropertyRecordDTO propertyRecord : records) {
          if(propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO valueRecordDTO =  (IValueRecordDTO) propertyRecord;
            if(valueRecordDTO.getLocaleID().equals("")) {
              isAllLocaleIdsApplicable = true;
              break;
            }
          }else if(propertyRecord instanceof ITagsRecordDTO) {
            isAllLocaleIdsApplicable = true;
            break;
          }
        }
      }else {
        isAllLocaleIdsApplicable = true;
      }
      
      applyDQRules(catalogDao, baseEntityIID, baseEntityClassifiers, baseEntityTaxonomies, removedBaseEntityClassifiers,
          isAllLocaleIdsApplicable, RuleType.kpi, new HashMap<>(), new HashMap<>(), new HashMap<>());
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
      requestMap.put("removedClassifiers", removedBaseEntityClassifiers);
      IUniquenessViolationDAO openUniquenessDAO = catalogDao.openUniquenessDAO();
      
      JSONObject detailsFromODB = CSConfigServer.instance().request(requestMap, "GetProductIdentifiersForClassifiers",
          catalogDao.getLocaleCatalogDTO().getLocaleID()); 
     
      evaluateProductIdentifiers(baseEntityIID, detailsFromODB, entityDao, openUniquenessDAO, catalogDao);
   
      RDBMSLogger.instance().info("End of Rule Event Handle");
    }
    catch (Exception ex) {
      RDBMSLogger.instance().exception(ex);
    }
  }
  

  public IChangedPropertiesDTO applyDQRules(ILocaleCatalogDAO catalogDao, long baseEntityIID, List<String> baseEntityClassifiers,
      List<String> baseEntityTaxonomies, List<String> removedBaseEntityClassifiers, boolean isAllLocaleIdsApplicable, RuleType ruleType,
      Map<String, IReferencedSectionElementModel> referencedElements, Map<String, IAttribute> referencedAttributes,
      Map<String, ITag> referencedTags) throws RDBMSException, CSFormatException
  {
    IChangedPropertiesDTO changedProperties = new ChangedPropertiesDTO();
    
    RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDao);
    List<String> filteredRules = new ArrayList<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          RuleCatalogDAS ruleDAS = new RuleCatalogDAS(connection);
          filteredRules.addAll(getRulesToEvaluate(catalogDao, records, baseEntityIID,
              baseEntityClassifiers, baseEntityTaxonomies, ruleDAS, removedBaseEntityClassifiers, ruleCatalogDAO, ruleType));
        });

    RuleCatalogDAO ruleCatalogDao = new RuleCatalogDAO(catalogDao, userSessionDao, userSessionDTO, referencedElements, referencedAttributes,
        referencedTags);
     
    //evaluate rules
    Set<IClassifierDTO> allAddedClassifiers = new HashSet<>();
    List<IBaseEntityDTO> baseEntitiesByIIDs = catalogDao.getBaseEntitiesByIIDs(List.of(String.valueOf(baseEntityIID)));
    IBaseEntityDAO baseEntityDAO = catalogDao.openBaseEntity(baseEntitiesByIIDs.get(0));

    for (String filteredRule : filteredRules) {
      IRuleDTO refreshDataQualityResult = ruleCatalogDao.refreshDataQualityResult(filteredRule, baseEntityDAO, isAllLocaleIdsApplicable);
      changedProperties.getAttributeCodes().addAll(refreshDataQualityResult.getAttributeCodes());
      changedProperties.getTagCodes().addAll(refreshDataQualityResult.getTagCodes());
      if (refreshDataQualityResult.getIsRuleCauseSatisfied()) {
        allAddedClassifiers.addAll(ruleCatalogDAO.getClassifiersToAdd(filteredRule, baseEntityIID, ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies)));
      }
    }


    if (!allAddedClassifiers.isEmpty()) {
      BaseEntityBulkUpdateDTO bulkUpdateDTO = prepareBulkUpdateDTOForTypeSwitch(allAddedClassifiers, catalogDao, baseEntityIID);
      BGPDriverDAO.instance().submitBGPProcess(CommonConstants.ADMIN, TYPE_SWITCH_SERVICE, "", IBGProcessDTO.BGPPriority.MEDIUM,
          new JSONContent(bulkUpdateDTO.toJSON()));
    }
    return changedProperties;
   }

  public IChangedPropertiesDTO applyDQRules(ILocaleCatalogDAO catalogDao, long baseEntityIID, List<String> baseEntityClassifiers,
      List<String> baseEntityTaxonomies, List<String> removedBaseEntityClassifiers, boolean isAllLocaleIdsApplicable,
      List<String> rulesToApply, RuleType ruleType, Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags) throws RDBMSException, CSFormatException
  {
    IChangedPropertiesDTO changedProperties = new ChangedPropertiesDTO();
    RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDao);
    List<String> filteredRules = new ArrayList<String>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          RuleCatalogDAS ruleDAS = new RuleCatalogDAS(connection);
          filteredRules.addAll(getFilteredRules(catalogDao, baseEntityIID, baseEntityClassifiers, baseEntityTaxonomies,
              ruleDAS, removedBaseEntityClassifiers, rulesToApply, ruleType));
        });
   
    RuleCatalogDAO ruleCatalogDao = new RuleCatalogDAO(catalogDao, userSessionDao, userSessionDTO, referencedElements, referencedAttributes,
        referencedTags);
    
    //evaluate rules
    Set<IClassifierDTO> allAddedClassifiers = new HashSet<IClassifierDTO>();

    List<IBaseEntityDTO> baseEntitiesByIIDs = catalogDao.getBaseEntitiesByIIDs(List.of(String.valueOf(baseEntityIID)));
    IBaseEntityDAO baseEntityDAO = catalogDao.openBaseEntity(baseEntitiesByIIDs.get(0));

    for (String filteredRule : filteredRules) {
      IRuleDTO refreshDataQualityResult = ruleCatalogDao.refreshDataQualityResult(filteredRule, baseEntityDAO, isAllLocaleIdsApplicable);
      changedProperties.getAttributeCodes().addAll(refreshDataQualityResult.getAttributeCodes());
      changedProperties.getTagCodes().addAll(refreshDataQualityResult.getTagCodes());
      if (refreshDataQualityResult.getIsRuleCauseSatisfied()) {
        allAddedClassifiers.addAll(ruleCatalogDAO.getClassifiersToAdd(filteredRule, baseEntityIID, ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies)));
      }
    }
   
    if (!allAddedClassifiers.isEmpty()) {
      BaseEntityBulkUpdateDTO bulkUpdateDTO = prepareBulkUpdateDTOForTypeSwitch(allAddedClassifiers, catalogDao, baseEntityIID);
      BGPDriverDAO.instance().submitBGPProcess(CommonConstants.ADMIN, TYPE_SWITCH_SERVICE, "", IBGProcessDTO.BGPPriority.MEDIUM,
          new JSONContent(bulkUpdateDTO.toJSON()));
    }
    
    return changedProperties;
   }

  public List<String> getRulesToEvaluate(ILocaleCatalogDAO catalogDao,
      List<IPropertyRecordDTO> records, long baseEntityIID, List<String> baseEntityClassifiers,
      List<String> baseEntityTaxonomies, RuleCatalogDAS ruleDAS,
      List<String> removedBaseEntityClassifiers, RuleCatalogDAO ruleCatalogDAO, RuleType ruleType)
      throws RDBMSException, SQLException, CSFormatException
  {
    List<String> rulesToEvaluate = ruleDAS.getRulesToEvaluate(catalogDao, ruleType);
    return getFilteredRules(catalogDao, baseEntityIID, baseEntityClassifiers, baseEntityTaxonomies,
        ruleDAS, removedBaseEntityClassifiers, rulesToEvaluate, ruleType);
  }

  private List<String> getFilteredRules(ILocaleCatalogDAO catalogDao, long baseEntityIID,
      List<String> baseEntityClassifiers, List<String> baseEntityTaxonomies, RuleCatalogDAS ruleDAS,
      List<String> removedBaseEntityClassifiers, List<String> changedRulesFromConfig, RuleType ruleType) throws RDBMSException, CSFormatException, SQLException
  {
    List<String> filteredRules = new ArrayList<>();
    List<Long> ruleExpressionsToBeDeleted = new ArrayList<>();
    ConfigurationDAO configurationInstance = ConfigurationDAO.instance();
    Set<Long> hierarchyIIDsForTaxonomies = new HashSet<>();

    List<IClassifierDTO> baseEntityTaxonomyClassifierDTOs = configurationInstance.getClassifierDtos(baseEntityTaxonomies);
    for(IClassifierDTO classifierDTO : baseEntityTaxonomyClassifierDTOs) {
      hierarchyIIDsForTaxonomies.addAll(classifierDTO.getHierarchyIIDs());
    }
    hierarchyIIDsForTaxonomies.remove(-1L);
    List<IClassifierDTO> hierarchyIIDClassifierDTOs = configurationInstance.getClassifiersByIID(hierarchyIIDsForTaxonomies);
    for(IClassifierDTO classifierDTO : hierarchyIIDClassifierDTOs) {
      baseEntityTaxonomies.add(classifierDTO.getClassifierCode());
    }
    List<String> rulesToEvaluate = ruleDAS.getRulesToEvaluate(catalogDao, ruleType);
    rulesToEvaluate.retainAll(changedRulesFromConfig);
    for (String ruleCode : rulesToEvaluate) {
      IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode(ruleCode);
      Collection<IRuleExpressionDTO> ruleExpressions = ruleByCode.getRuleExpressions();
      for (IRuleExpressionDTO ruleExpressionDTO : ruleExpressions) {

        ICSERule parseRule = (new CSEParser()).parseRule(ruleExpressionDTO.getRuleExpression());
        ICSEEntityFilterNode entityFilter = parseRule.getScope()
            .getEntityFilter();
        // if no classification filter is present
        if (entityFilter == null) {
          continue;
        }
        Collection<String> classifierCodes = entityFilter.getIncludingClassifiers();

        Map<ClassifierType, List<String>> groupedClassifiers = ConfigurationDAO.instance()
            .groupClassifiers(classifierCodes);

        List<String> classes = groupedClassifiers.getOrDefault(ClassifierType.CLASS,
            new ArrayList<>());
        List<String> taxonomies = Stream
            .of(groupedClassifiers.getOrDefault(ClassifierType.TAXONOMY, new ArrayList<>())
                .stream(),
                groupedClassifiers.getOrDefault(ClassifierType.MINOR_TAXONOMY, new ArrayList<>())
                    .stream())
            .flatMap(i -> i)
            .collect(Collectors.toList()); 

        Boolean evaluateClassifierFilters = evaluateClassifierFilters(baseEntityClassifiers,
            baseEntityTaxonomies, taxonomies, classes);
        if (evaluateClassifierFilters) {
          filteredRules.add(ruleCode);
        }else {
          ruleExpressionsToBeDeleted.add(ruleExpressionDTO.getRuleExpressionIId());
        }



        evaluateKpiIfTaxonomyIsRemoved(baseEntityIID, baseEntityTaxonomies, removedBaseEntityClassifiers, catalogDao, taxonomies);
        break;
      }
    }
    if(!ruleExpressionsToBeDeleted.isEmpty())
    {
      ruleDAS.deleteExpressionsForSaveRule(ruleExpressionsToBeDeleted,baseEntityIID);
    }
    return filteredRules;
  }
  public void getBaseEntityClassifiersAndTaxonomies(ILocaleCatalogDAO catalogDao, IBaseEntityDAO entityDao,
      List<String> baseEntityClassifiers, List<String> baseEntityTaxonomies)
      throws RDBMSException, CSFormatException
  {
    List<IClassifierDTO> classifiers = entityDao.getClassifiers();

    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType()
          .equals(ClassifierType.CLASS)) {
        baseEntityClassifiers.add(classifier.getClassifierCode());
      }
      else {
        baseEntityTaxonomies.add(classifier.getClassifierCode());
      }
    }

    baseEntityClassifiers.add(entityDao.getBaseEntityDTO().getNatureClassifier().getCode());

  }

  @SuppressWarnings("unchecked")
  public void evaluateProductIdentifiers(long baseEntityIID, JSONObject detailsFromODB,
      BaseEntityDAO entityDao, IUniquenessViolationDAO uniquenessDAO, ILocaleCatalogDAO localCatalogDAO)
      throws RDBMSException, CSFormatException
  {
    List<IUniquenessViolationDTO> entryDataForBackgroundProcess = new ArrayList<>();
    Map<String, List<Long>> productIdentifiers = (Map<String, List<Long>>) detailsFromODB.get(IDataRulesHelperModel.PRODUCT_IDENTIFIERS);
    // evaluate identifier
    if (productIdentifiers == null || productIdentifiers.isEmpty()) {
      return;
    }
    for (Map.Entry<String, List<Long>> entry : productIdentifiers.entrySet()) {
      List<IPropertyDTO> propertyDTO = new ArrayList<>();
      List<Long> productIdentifierIIds = new ArrayList<>(entry.getValue());
      long classifierIID = Long.parseLong(entry.getKey());
      
      for (Long propertyIID : productIdentifierIIds) {
        propertyDTO.add(ConfigurationDAO.instance().getPropertyByIID(propertyIID));
      }
      IBaseEntityDTO propertyRecords = entityDao.loadPropertyRecords(propertyDTO.toArray(new IPropertyDTO[] {}));

      uniquenessDAO.deleteBeforeEvaluateIdentifier(baseEntityIID, productIdentifierIIds , classifierIID);
      for (IPropertyRecordDTO propertyRecord : propertyRecords.getPropertyRecords()) {
        
        long identifierIID = propertyRecord.getProperty().getPropertyIID();
        
        List<Long> violatedEntites = uniquenessDAO.evaluateProductIdentifier(identifierIID, baseEntityIID, classifierIID, 
            entityDao.getBaseEntityDTO().getCatalog().getCatalogCode());
        
        if(violatedEntites.size() > 1) {
          violatedEntites.remove(baseEntityIID);
          List<IUniquenessViolationDTO> toInsertViolatedEntities = new ArrayList<>();
          
          for(long targetEntityIID: violatedEntites) {
            IUniquenessViolationDTO uniquenessViolationDTO = localCatalogDAO.newUniquenessViolationBuilder(baseEntityIID, 
                targetEntityIID, identifierIID, classifierIID)
                .build();
          
            toInsertViolatedEntities.add(uniquenessViolationDTO);
            
            IUniquenessViolationDTO DtoForBackGroundProcess = localCatalogDAO.newUniquenessViolationBuilder(targetEntityIID, 
                baseEntityIID, identifierIID, classifierIID)
            .build();
            
            entryDataForBackgroundProcess.add(DtoForBackGroundProcess);
          }
          uniquenessDAO.insertViolatedEntity(toInsertViolatedEntities);
        }
        
      }
      
    }
    
    if(entryDataForBackgroundProcess.size() >= 1) {
      
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
      
      IUniquenessDTO entryData = new UniquenessDTO();
      
      entryData.setUniquenessViolationDTOs(entryDataForBackgroundProcess);
      BGPDriverDAO.instance()
      .submitBGPProcess("Admin", SERVICE, "", userPriority,
          new JSONContent(entryData.toJSON()));
    }
  }

  private BaseEntityBulkUpdateDTO prepareBulkUpdateDTOForTypeSwitch(Set<IClassifierDTO> allAddedClassifiers, ILocaleCatalogDAO catalogDao, long baseEntityIID)
  {
    ILocaleCatalogDTO localeCatalogDTO = catalogDao.getLocaleCatalogDTO();
    BaseEntityBulkUpdateDTO bulkUpdateDTO = new BaseEntityBulkUpdateDTO();
    
    String userId = userSessionDTO.getUserName();

    bulkUpdateDTO.setBaseEntityIIDs(Arrays.asList(baseEntityIID));
    bulkUpdateDTO.getAddedClassifiers().addAll(allAddedClassifiers);
    bulkUpdateDTO.setRemovedClassifiers(new HashSet<>());
    bulkUpdateDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    bulkUpdateDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    bulkUpdateDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    bulkUpdateDTO.setUserId(userId);

    return bulkUpdateDTO;
  }

  private static final String Q_EMPTY_IDENTIFIER_VIOLATION = "SELECT propertyIID from pxp.baseentityqualityrulelink WHERE baseentityIID = %s "
      + "AND ruleexpressionIID = %s AND propertyIID in (%s)";
  
  @SuppressWarnings("unused")
  private void fillViolationForEmptyIdentifier(long baseEntityIID,
      Map.Entry<String, List<Long>> entry, List<Long> productIdentifierIIds) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          String format = String.format(Q_EMPTY_IDENTIFIER_VIOLATION, baseEntityIID,
              entry.getKey(), Text.join(",", productIdentifierIIds));
          PreparedStatement stmt = connection.prepareStatement(format);
          stmt.execute();
          
          IResultSetParser result = connection.getResultSetParser(stmt.getResultSet());
          while (result.next()) {
            long long1 = result.getLong("propertyIID");
            productIdentifierIIds.remove(long1);
          }

          RuleCatalogDAS das = new RuleCatalogDAS( connection);
          for (Long productIdentifierIId : productIdentifierIIds) {
           das.insertIdentifierViolation(Long.parseLong(entry.getKey()), null, baseEntityIID, productIdentifierIId, "This field is mandatory! Please enter a value.");
          }
        });
  }
  
  public void evaluateDQMustShould(long baseEntityIID, IDataRulesHelperModel dataRules, IBaseEntityDAO entityDao, ILocaleCatalogDAO catalogDAO) throws RDBMSException, CSFormatException
  {
    List<Long> nonEmptyPropertyIds = fillMandatoryViolations(baseEntityIID, dataRules, entityDao, catalogDAO);
    removeMandatoryViolations(baseEntityIID, nonEmptyPropertyIds, catalogDAO);
  }
  
  @SuppressWarnings("unchecked")
  public List<Long>  fillMandatoryViolations(long baseEntityIID, IDataRulesHelperModel dataRules, IBaseEntityDAO entityDao, ILocaleCatalogDAO catalogDao)
      throws RDBMSException, CSFormatException
  {
    List<Long> nonEmptyPropertyIds = new ArrayList<Long>();
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    List<Long> removedMandatoryIdentifiers = dataRules.getRemovedMandatoryIdentifiers();
    List<Long> mustPropertyIds = dataRules.getMust();
    List<Long> shouldPropertyIds = dataRules.getShould();

    List<Long> mandatoryPropertyIds = ListUtils.sum(mustPropertyIds, shouldPropertyIds);
    for (Long mandatoryPropertyIID : mandatoryPropertyIds) {
      propertyDTOs.add(ConfigurationDAO.instance().getPropertyByIID(mandatoryPropertyIID));
        }

    if(!propertyDTOs.isEmpty()){
      Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = catalogDao.getPropertyRecordsForEntities(Set.of(baseEntityIID),
          propertyDTOs.toArray(new IPropertyDTO[0]));
      Set<IPropertyRecordDTO> propertyRecords = propertyRecordsForEntities.computeIfAbsent(baseEntityIID, x-> new HashSet<>());

      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        long propertyIID = propertyRecord.getProperty().getPropertyIID();
        if(propertyRecord instanceof ValueRecordDTO) {
          if (((IValueRecordDTO) propertyRecord).getValue().isEmpty()) {
            continue;
          }
        }else {
          if (((TagsRecordDTO) propertyRecord).getTagValueCodes().isEmpty()) {
            continue;
          }
        }
        nonEmptyPropertyIds.add(propertyIID);
      }
    }
    mandatoryPropertyIds.removeAll(nonEmptyPropertyIds);
      nonEmptyPropertyIds.addAll(removedMandatoryIdentifiers);
    fillEmptyMandatoryViolations(baseEntityIID, mandatoryPropertyIds, shouldPropertyIds, dataRules.getTranslatablePropertyIIDs(), catalogDao);
    return nonEmptyPropertyIds;
  }

  public void removeMandatoryViolations(long baseEntityIID, List<Long> nonEmptyPropertyIds, ILocaleCatalogDAO catalogDAO) throws RDBMSException
  {
    if (nonEmptyPropertyIds.isEmpty()) {
      return;
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      RuleCatalogDAS ruleDAS = new RuleCatalogDAS(connection);
      ruleDAS.deleteNonEmptyMandatoryViolations(connection, baseEntityIID, nonEmptyPropertyIds, catalogDAO.getLocaleCatalogDTO().getLocaleID());
    });
  }
  
  private static final String Q_EMPTY_MANDATORY_VIOLATION = "SELECT propertyIID from pxp.baseentityqualityrulelink WHERE baseentityIID = %s "
      + "AND ruleexpressionIID = -1 AND propertyIID in (%s) and (localeid = '%s' or localeid = 'NAL') ";
  
  public void fillEmptyMandatoryViolations(long baseEntityIID, List<Long> mandatoryPropertyIds, List<Long> shouldPropertyIds, List<Long> translatablePropertIIDs,
      ILocaleCatalogDAO catalogDAO) throws RDBMSException
  {
    if (mandatoryPropertyIds.isEmpty()) {
      return;
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          /*String format = String.format(Q_EMPTY_MANDATORY_VIOLATION, baseEntityIID, Text.join(",", mandatoryPropertyIds));
          PreparedStatement stmt = connection.prepareStatement(format);
          stmt.execute();
          
          IResultSetParser result = connection.getResultSetParser(stmt.getResultSet());
          while (result.next()) {
            long long1 = result.getLong("propertyIID");
            mandatoryPropertyIds.remove(long1);
          }*/
      
      RuleCatalogDAS das = new RuleCatalogDAS(connection);
      for (Long mandatoryPropertyId : mandatoryPropertyIds) {
        int flagOrdinal = ICSEPropertyQualityFlag.QualityFlag.$red.ordinal();
        String message = "This field is mandatory! Please enter a value.";
        if (shouldPropertyIds.contains(mandatoryPropertyId)) {
          flagOrdinal = ICSEPropertyQualityFlag.QualityFlag.$orange.ordinal();
          message = "This field should have a value! Please enter a value.";
        }
        String localeID = null;
        if(translatablePropertIIDs.contains(mandatoryPropertyId)) {
          localeID = catalogDAO.getLocaleCatalogDTO().getLocaleID();
        }
        das.insertMandatoryViolation(localeID, baseEntityIID, mandatoryPropertyId, flagOrdinal, message);
      }
    });
  }
    
  private void evaluateKpiIfTaxonomyIsRemoved(long baseEntityIID, List<String> baseEntityTaxonomies,
      List<String> removedBaseEntityClassifiers, ILocaleCatalogDAO catalogDAO, List<String> taxonomies) throws RDBMSException
  {
    RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDAO); 
    if (!removedBaseEntityClassifiers.isEmpty()) {
      Boolean toEvaluateKpi = ListUtils.intersection(baseEntityTaxonomies, taxonomies).isEmpty();
      if (toEvaluateKpi == true) {
        Map<Long, List<Long>> ruleExpressionVsTargetEntityIIDs = ruleCatalogDAO.getAllTargetEntitiesForKpiUniqueness(baseEntityIID);

        for (Long ruleExpressionIID : ruleExpressionVsTargetEntityIIDs.keySet()) {
          List<Long> listOfTargetEntityIIDs = ruleExpressionVsTargetEntityIIDs.get(ruleExpressionIID);
          if (listOfTargetEntityIIDs.size() == 1) {
            ruleCatalogDAO.updateTargetBaseEntityforUniquenessBlock(listOfTargetEntityIIDs, ruleExpressionIID, (double) 1);
          }
        }

        ruleCatalogDAO.deleteKpiOnBaseEntityDelete(baseEntityIID);
        ruleCatalogDAO.deleteFromKpiUniquenessViolation(baseEntityIID);
      }
    }
  }

  public Boolean evaluateClassifierFilters(List<String> baseEntityClasses,
      List<String> baseEntityTaxonomies, List<String> taxonomies, List<String> classes)
  {
    // don't evaluate if empty
    Boolean contentClasses = !baseEntityClasses.isEmpty();
    Boolean contentTaxonomy = !baseEntityTaxonomies.isEmpty();
    Boolean ruleClasses = !classes.isEmpty();
    Boolean ruleTaxonomy = !taxonomies.isEmpty();
    
    //if no class or taxonomy is present on rule then dont evaluate for anything.
    if(!ruleClasses && !ruleTaxonomy) {
      return false;
    }
    // if classes and taxonomy are both present in rules,
    // then need to evaluate if atleast one taxonomy AND one class is present in
    // base entity.
    if (contentTaxonomy && ruleTaxonomy && contentClasses && ruleClasses) {
      return !ListUtils.intersection(baseEntityClasses, classes)
          .isEmpty()
          && !ListUtils.intersection(baseEntityTaxonomies, taxonomies)
              .isEmpty();
    }
    // if only classes are present in rules.
    // then evaluation should be done to check if atleast one class is present
    // in base entity.
    if (ruleTaxonomy && !ruleClasses && contentTaxonomy) {
      return !ListUtils.intersection(baseEntityTaxonomies, taxonomies)
          .isEmpty();
    }
    // if only classes are present in rules.
    // then evaluation should be done to check if atleast one class is present
    // in base entity.
    if (ruleClasses && !ruleTaxonomy && contentClasses) {
      return !ListUtils.intersection(baseEntityClasses, classes)
          .isEmpty();
    }
    return false;
  }
  
  public IChangedPropertiesDTO initiateRuleHandling(IBaseEntityDAO baseEntityDAO, ILocaleCatalogDAO localCatalogDAO,
      Boolean isClassifierModified, Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags) throws Exception
  {
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    getBaseEntityClassifiersAndTaxonomies(localCatalogDAO, baseEntityDAO, baseEntityClassifiers, baseEntityTaxonomies);
    Boolean isLocaleIdsApplicable = isClassifierModified ? isClassifierModified : evaluateLocaleIdsApplicable(baseEntityDTO);
    IChangedPropertiesDTO changedProperties = applyDQRules(localCatalogDAO, baseEntityDTO.getBaseEntityIID(), baseEntityClassifiers, baseEntityTaxonomies,
        new ArrayList<String>(), isLocaleIdsApplicable, RuleType.dataquality, referencedElements, referencedAttributes, referencedTags);
    IDataRulesHelperModel dataRules = prepareDataForMustShouldIdentifiers(referencedElements, referencedAttributes, referencedTags);
    evaluateDQMustShould(baseEntityDTO.getBaseEntityIID(), dataRules, baseEntityDAO, localCatalogDAO);
    return changedProperties;
  }

  public IChangedPropertiesDTO initiateRuleHandlingForSave(IBaseEntityDAO baseEntityDAO, ILocaleCatalogDAO localCatalogDAO,
      Boolean isClassifierModified, Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags) throws Exception
  {
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Set<IClassifierDTO> classifiers = baseEntityDTO.getOtherClassifiers();

    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        baseEntityClassifiers.add(classifier.getClassifierCode());
      }
        baseEntityTaxonomies.add(classifier.getClassifierCode());
    }
    baseEntityClassifiers.add(baseEntityDAO.getBaseEntityDTO().getNatureClassifier().getCode());

    Boolean isLocaleIdsApplicable = isClassifierModified ? isClassifierModified : evaluateLocaleIdsApplicable(baseEntityDTO);
    IChangedPropertiesDTO changedProperties = applyDQRules(localCatalogDAO, baseEntityDTO.getBaseEntityIID(), baseEntityClassifiers, baseEntityTaxonomies,
        new ArrayList<>(), isLocaleIdsApplicable, RuleType.dataquality, referencedElements, referencedAttributes, referencedTags);

    IDataRulesHelperModel dataRules = prepareDataForMustShouldIdentifiers(referencedElements, referencedAttributes, referencedTags);

    evaluateDQMustShould(baseEntityDTO.getBaseEntityIID(), dataRules, baseEntityDAO, localCatalogDAO);

    return changedProperties;
  }
  
  public Boolean evaluateLocaleIdsApplicable(IBaseEntityDTO baseEntityDto)
  {
    Boolean isAllLocaleIdsApplicable = false;
    for (IPropertyRecordDTO propertyRecord : baseEntityDto.getPropertyRecords()) {
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
        if (valueRecordDTO.getLocaleID().equals("")) {
          isAllLocaleIdsApplicable = true;
          break;
        }
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        isAllLocaleIdsApplicable = true;
        break;
      }
    }
    return isAllLocaleIdsApplicable;
  }
  
  public IDataRulesHelperModel prepareDataForMustShouldIdentifiers(Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags)
  {
    IDataRulesHelperModel dataRules = new DataRulesHelperModel();
    List<Long> must = new ArrayList<Long>();
    List<Long> should = new ArrayList<Long>();
    List<Long> translatablePropertyIIDs = new ArrayList<Long>();
    
    referencedElements.values().forEach(referencedElement -> {
      
      Long propertyIid = null;
      
      if (referencedElement.getType().equals("attribute")) {
        IAttribute referencedAttribute = referencedAttributes.get(referencedElement.getId());
        propertyIid = referencedAttribute.getPropertyIID();
        if (referencedAttribute.getIsTranslatable()) {
          translatablePropertyIIDs.add(propertyIid);
        }
      }
      else if (referencedElement.getType().equals("tag")) {
        propertyIid = referencedTags.get(referencedElement.getId()).getPropertyIID();
      }
      
      if (propertyIid != null) {
        if (referencedElement.getIsMandatory() && !must.contains(propertyIid)) {
          must.add(propertyIid);
        }
        else if (referencedElement.getIsShould() && !should.contains(propertyIid)) {
          should.add(propertyIid);
        }
      }
    });
    
    dataRules.setMust(must);
    dataRules.setShould(should);
    dataRules.setTranslatablePropertyIIDs(translatablePropertyIIDs);
    
    return dataRules;
  }
}
