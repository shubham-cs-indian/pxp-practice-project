package com.cs.core.rdbms.process.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.calculation.CSELiteralOperand;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.RuleDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IRuleDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.dto.KPIResultDTO;
import com.cs.core.rdbms.process.dto.RuleResultDTO;
import com.cs.core.rdbms.process.dto.RuleViolationDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IKPIResultDTO;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.rdbms.services.resolvers.RuleResolver;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.icsexpress.definition.ICSETagValue;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@SuppressWarnings("unchecked")
public class RuleCatalogDAO implements IRuleCatalogDAO {
  
  private LocaleCatalogDAO localCatalogDAO;
  private IUserSessionDAO userSessionDAO;
  private IUserSessionDTO userSessionDTO;
  Map<String, ILocaleCatalogDAO> localCatalogDAOs = new HashMap<>();
  private Map<String, IReferencedSectionElementModel> referencedElements = new HashMap<>();
  private Map<String, IAttribute> referencedAttributes = new HashMap<>();
  private Map<String, ITag> referencedTags = new HashMap<>();
  
  public RuleCatalogDAO(ILocaleCatalogDAO localeCatalogDAO)
  {
    this.localCatalogDAO = (LocaleCatalogDAO) localeCatalogDAO;
  }
  
  public RuleCatalogDAO(ILocaleCatalogDAO localCatalogDAO, IUserSessionDAO userSessionDAO, IUserSessionDTO userSessionDTO)
  {
    this.localCatalogDAO = (LocaleCatalogDAO) localCatalogDAO;
    this.userSessionDAO = userSessionDAO;
    this.userSessionDTO = userSessionDTO;
  }
  
  public RuleCatalogDAO(ILocaleCatalogDAO localCatalogDAO, IUserSessionDAO userSessionDAO, IUserSessionDTO userSessionDTO,
      Map<String, IReferencedSectionElementModel> referencedElements, Map<String, IAttribute> referencedAttributes,
      Map<String, ITag> referencedTags)
  {
    this.localCatalogDAO = (LocaleCatalogDAO) localCatalogDAO;
    this.userSessionDAO = userSessionDAO;
    this.userSessionDTO = userSessionDTO;
    this.referencedElements = referencedElements;
    this.referencedAttributes = referencedAttributes;
    this.referencedTags = referencedTags;
  }

  @Override
  public ILocaleCatalogDTO getCatalog()
  {
    return localCatalogDAO.getLocaleCatalogDTO();
  }
  
  private static final String KPI_DASHBOARD_GROUP_BY_QUERY_FOR_RULE_CODE_AND_BASEENTITY   = "select rulecode, avg(kpi) AS kpi, baseentityiid from pxp.kpi where "
      + "ruleCode =  \'%s\' and localeid = \'%s\' and catalogCode = \'%s\' and organizationCode = \'%s\'";
  
  private static final String KPI_DASHBOARD_GROUP_BY_QUERY_FOR_RULE_CODE           = "select rulecode, avg(kpi) AS kpi from pxp.kpi where "
      + "ruleCode =  \'%s\' and localeid = \'%s\' and catalogCode = \'%s\' and organizationCode = \'%s\'";
  
  private static final String Q_GET_ALL_VALUE_RECORDS = "select * from pxp.AllValueRecord where propertyiid = ? and entityiid = ?";
  private static final String Q_GET_ALL_TAG_RECORDS   = "select * from pxp.AllTagsRecord where propertyiid = ? and entityiid = ?";
  private static final String Q_WITH_LOCALE_ID        = " and localeid = '%s'";
  private static final String DRILLDOWN_FOR_CLASSIFIER = "select rulecode, avg(kpi) AS kpi from (SELECT rulecode, "
      + "hierarchyiids, classifiercode, avg(kpi) AS kpi, baseentityiid, localeid, catalogcode, organizationcode "
      + "from pxp.kpi group by  rulecode, hierarchyiids, classifiercode, "
      + "localeid, catalogcode, organizationcode,baseentityiid) AS SUBQUERY where ruleCode =  \'%s\' "
      + "and localeid = \'%s\' and catalogCode = \'%s\' and organizationCode = \'%s\' ";  
  
  @Override
  public IRuleResultDTO getKPIRuleResult(String ruleCode, String entityCode, String localeID, String type, 
      List<String> classifierInfo, List<String> tagInfo) throws RDBMSException
  {
    
    ILocaleCatalogDTO localCatDTO = localCatalogDAO.getLocaleCatalogDTO();
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    
    StringBuilder queryForClassifier = new StringBuilder();
    
    if(!type.equals("tag")) {
      
      IClassifierDTO classifierDTO = configurationDAO.getClassifierByCode(entityCode);
      
      ClassifierType classifierType = classifierDTO.getClassifierType();
      if(classifierType == ClassifierType.CLASS) {
        queryForClassifier.append("and classifiercode = '"+entityCode+"'");
      }else {
        queryForClassifier.append("and hierarchyiids && '{\" " +classifierDTO.getClassifierIID()+ "\"}' ");
      }
    }
    
    if (!classifierInfo.isEmpty() && queryForClassifier.length() == 0) {
      
      String pathClassifierCode = classifierInfo.get(classifierInfo.size() - 1);
      
      IClassifierDTO classifierDTO = configurationDAO.getClassifierByCode(pathClassifierCode);
      
      ClassifierType classifierType = classifierDTO.getClassifierType();
      if (classifierType == ClassifierType.CLASS) {
        queryForClassifier.append("and classifiercode = '" + pathClassifierCode + "'");
      }
      else {
        queryForClassifier.append("and hierarchyiids && '{\" " + classifierDTO.getClassifierIID() + "\"}' ");
      }
      
    }
    
    StringBuilder finalQuery = new StringBuilder();
    
    Iterator<String> iterator = tagInfo.iterator();
    while(iterator.hasNext()) {
      finalQuery.append(String.format(KPI_DASHBOARD_GROUP_BY_QUERY_FOR_RULE_CODE_AND_BASEENTITY, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
      finalQuery.append(queryForClassifier);
      finalQuery.append("and tagvaluecode = '" +iterator.next() +"' ");
      if(iterator.hasNext()) {
        finalQuery.append(" group by rulecode, baseentityiid");
        finalQuery.append(" intersect ");
      }
    }
    
    if(!tagInfo.isEmpty()) {
      
      if(type.equals("tag")) {
        finalQuery.append(" group by rulecode, baseentityiid");
        finalQuery.append(" intersect ");
        
        finalQuery.append(String.format(KPI_DASHBOARD_GROUP_BY_QUERY_FOR_RULE_CODE_AND_BASEENTITY, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        finalQuery.append("and tagvaluecode = '" +entityCode +"' ");
      } 
      finalQuery.append(" group by rulecode, baseentityiid");
      
      finalQuery.insert(0, "select rulecode, avg(kpi) AS kpi from(");
      finalQuery.append(") AS SUBQUERY GROUP BY rulecode");
    }else {
      
      if(type.equals("tag")) {
        
        finalQuery.append(String.format(KPI_DASHBOARD_GROUP_BY_QUERY_FOR_RULE_CODE, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        finalQuery.append("and tagvaluecode = '" +entityCode +"' ");
        finalQuery.append(" group by rulecode");
      }else {
        
        finalQuery.append(String.format(DRILLDOWN_FOR_CLASSIFIER, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        
        finalQuery.append(" group by rulecode");
      }
    }
    
    RuleResultDTO ruleResult = new RuleResultDTO(RuleType.kpi, ruleCode);
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
            PreparedStatement stmt = currentConn.prepareStatement(finalQuery.toString());
              stmt.execute();
              IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());

              if (result.next()) {
                Double kpi = result.getDouble("kpi");
                //long totalNumberOfContents = result.getInt("totalCount");
                ruleResult.fillKPI( kpi, 0l);
              }
          });    
        return ruleResult;
  }
  
  @Override
  public Map<String,IRuleResultDTO> getKPIRuleResultsForGetAll(String cteQuery) throws RDBMSException
  {
    
        Map<String,IRuleResultDTO> resultMap = new HashMap<>();
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
            PreparedStatement stmt = currentConn.prepareStatement(cteQuery);
              stmt.execute();
              IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());

              while (result.next()) {
                Double kpi = result.getDouble("kpi");
                String ruleCode = result.getString("rulecode");
                String key = result.getString("key");
                RuleResultDTO ruleResult = new RuleResultDTO(RuleType.kpi, ruleCode);
                ruleResult.fillKPI( kpi, 0l);
                resultMap.put(key, ruleResult);
              }
          });    
        return resultMap;
  }

  private IRuleDTO applyRules(IConfigRuleDTO rule, String localeID, Boolean isAllLocaleIdsApplicable, IBaseEntityDAO entityDAO)
      throws  RDBMSException
  {
    long baseEntityIID = entityDAO.getBaseEntityDTO().getBaseEntityIID();
    IBaseEntityDTO entity = entityDAO.getBaseEntityDTO();
    IRuleDTO changedProperties = new RuleDTO();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {

      RuleCatalogDAS ruleCatalogDAS = new RuleCatalogDAS( currentConn);
      Collection<IRuleExpressionDTO> ruleExpressions = rule.getRuleExpressions();
      for (IRuleExpressionDTO ruleExpressionDTO : ruleExpressions) {
            String ruleExpression = ruleExpressionDTO.getRuleExpression();
            if(rule.getType().equals(RuleType.dataquality)) {
              Boolean SkipRuleEvaluation = false;
              Collection<Long> whenPropertyIIDs = ruleExpressionDTO.getWhenPropertyIIDs();
              for(Long propertyIID : whenPropertyIIDs) {
                IPropertyDTO property = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
                if(!referencedElements.containsKey(property.getPropertyCode())) {
                  SkipRuleEvaluation = true;
                  break;
                }
              }
            
              if(SkipRuleEvaluation) {
                continue;
              }
            }
            ICSERule parseRule = (new CSEParser()).parseRule(ruleExpression);
            long ruleExpressionIId = ruleExpressionDTO.getRuleExpressionIId();
            ICSEScope scope = parseRule.getScope();
            ICSEEntityFilterNode entityFilter = scope.getEntityFilter();
            Collection<String> includingClassifiers = entityFilter.getIncludingClassifiers();

            String catalogCode = entity.getLocaleCatalog().getCatalogCode();
            String organizationCode = entity.getLocaleCatalog().getOrganizationCode();
            ICSECalculationNode evaluation = parseRule.getEvaluation();

            Collection<String> catalogCodes = new ArrayList<String>(); //ruleExpressionDTO.getCatalogCodes();
            catalogCodes.add(catalogCode);
            Collection<String> ruleLocaleIds = ruleExpressionDTO.getLocaleIDs();
            Collection<String> organizationCodes = new ArrayList<String>(); //ruleExpressionDTO.getOrganizationCodes();
            organizationCodes.add(organizationCode);
            List<String> localeIdsToEvaluate = new ArrayList<>();
            
            if(isAllLocaleIdsApplicable && rule.getType().equals(ICSERule.RuleType.dataquality) && !ruleLocaleIds.isEmpty()) { // if it is data quality rule and language dependent.
              localeIdsToEvaluate = ListUtils.intersection(entity.getLocaleIds(), (List) ruleLocaleIds);
            }
            else if(isAllLocaleIdsApplicable) {
              localeIdsToEvaluate = entity.getLocaleIds();
            }
            else {
              localeIdsToEvaluate.add(localeID);
            }
            
            for(String localeId : localeIdsToEvaluate) {
            
               if(localCatalogDAOs.containsKey(localeId)) {
                localCatalogDAO = (LocaleCatalogDAO) localCatalogDAOs.get(localeId);
              }else {
                ILocaleCatalogDTO catalogDTO = new LocaleCatalogDTO(localeId, catalogCode, organizationCode);
                localCatalogDAO = (LocaleCatalogDAO) userSessionDAO.openLocaleCatalog(userSessionDTO, catalogDTO);
                localCatalogDAOs.put(localeId, localCatalogDAO);
              }
              
              RuleResolver ruleResolver = new RuleResolver( currentConn, localCatalogDAO, catalogCodes, ruleLocaleIds, 
                  organizationCodes, new StringBuilder(), includingClassifiers, referencedElements);
              
              ICSELiteralOperand result = evaluation != null ? ruleResolver.getResult(baseEntityIID, evaluation)
                  : new CSELiteralOperand(LiteralType.Boolean, true);

              checkForUniquenessBlock(currentConn, ruleExpressionIId, ruleResolver, result, baseEntityIID, ruleCatalogDAS, localeId);

              String localeIdToDelete = ruleLocaleIds.isEmpty() ? "" : localeId;
              ruleCatalogDAS.deleteViolation(currentConn, localeIdToDelete, ruleExpressionDTO.getRuleExpressionIId(), baseEntityIID);
              switch (rule.getType()) {
                case dataquality:
                  
                  if (result.asBoolean()) {
                    Map<IRootConfigDTO, ICSELiteralOperand> dataQualityResult = ruleResolver.evaluateRuleEffects(parseRule.getActionList(), ruleExpressionIId, baseEntityIID);
                    propagateNormalization(dataQualityResult, entityDAO, localeId, ruleLocaleIds, changedProperties);
                    changedProperties.setIsRuleCauseSatisfied(true);
                  }
                  break;
                case kpi:
                  ruleCatalogDAS.insertKPIevaluation(result.asDouble(), baseEntityIID, ruleExpressionIId, localeId);
                  break;
                default:
                  break;
              }
            }
          }
        });
    return changedProperties;
  }


  /**
   * 
   * Updating the ruleExpressionIID which belongs to uniquness block.
   * Updating the previously violated baseEntities.
   * Updating current baseEntity.
   * Sending the current violated baseEntites to background process
   * @param localeID 
   */
  private void checkForUniquenessBlock(RDBMSConnection currentConn, long ruleExpressionIId, RuleResolver ruleResolver,
      ICSELiteralOperand result, long baseEntityIID, RuleCatalogDAS ruleCatalogDAS, String localeID) throws RDBMSException, SQLException, CSFormatException
  {
    List<Long> duplicateBaseEntityIIDsAfterEvaluation = new ArrayList<>();
    StringBuilder queryForUniqueness = ruleResolver.queryForUniqueness;
    
    if(ruleResolver.queryForUniqueness.length() != 0) {
      PreparedStatement stmt = currentConn.prepareStatement(queryForUniqueness.toString());
      stmt.execute();
      
      ResultSet resultSet = stmt.getResultSet();
      while(resultSet.next()) {
        duplicateBaseEntityIIDsAfterEvaluation.add(resultSet.getLong("entityiid"));
      }
      
      // to get All the BaseEntities which were previously violated before updating current baseEntity.
      Map<Long, List<Long>> baseEntityIIdsToUpdateBeforeEvalution = getAllTargetEntitiesForKpiUniqueness(baseEntityIID);
      List<Long> entitiesToUpdateBeforeEvaluation = baseEntityIIdsToUpdateBeforeEvalution.get(ruleExpressionIId);
      baseEntityIIdsToUpdateBeforeEvalution.remove(baseEntityIID);
      
      // to update All the BaseEntities which were previously violated before updating current baseEntity.
      if(entitiesToUpdateBeforeEvaluation != null && entitiesToUpdateBeforeEvaluation.size() == 1 
          && !duplicateBaseEntityIIDsAfterEvaluation.contains(entitiesToUpdateBeforeEvaluation.get(0))) {
        //ruleCatalogDAS.insertKPIevaluation((double)1, entitiesToUpdateBeforeEvaluation.get(0), ruleExpressionIId, localeID);
        updateTargetBaseEntityforUniquenessBlock(entitiesToUpdateBeforeEvaluation, ruleExpressionIId, (double)1);
      }
      
      // Updating the current baseEntity and violated baseentities to be send to background process.
      duplicateBaseEntityIIDsAfterEvaluation.remove(baseEntityIID);
      
      // delete from pxp.kpiUniqeunessViolation table before updating
      deleteKpiFromKpiUniquenessViolation(baseEntityIID, duplicateBaseEntityIIDsAfterEvaluation, ruleExpressionIId);
      
      //checking wheather current baseEntity is Unique or Violated
      if(duplicateBaseEntityIIDsAfterEvaluation.size() == 0) {
        //If unique then update the current baseEntity
        ((CSELiteralOperand) result).setValueAndType((double)1, LiteralType.Number);
      }else {
        
        //If violated then updating current baseEntity and violated baseEntities as well
        insertKpiUniqunessViolation(baseEntityIID, duplicateBaseEntityIIDsAfterEvaluation, ruleExpressionIId);
        ruleCatalogDAS.insertKPIevaluation((double)0, baseEntityIID, ruleExpressionIId, localeID);
        
        // Sending violated entities to background Process to update.
        /*IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
        
        IUniquenessDTO entryData = new UniquenessDTO();
        
        IKpiUniqunessDTO kpiUniqunessDTO = new KpiUniqunessDTO();
        kpiUniqunessDTO.setBaseEntityIID(duplicateBaseEntityIIDsAfterEvaluation);
        kpiUniqunessDTO.setRuleExpressionIID(ruleExpressionIId);
        kpiUniqunessDTO.setSourceIID(baseEntityIID);
        kpiUniqunessDTO.setResult((double)0);
        
        
        entryData.setKpiUniqunessDTOs(kpiUniqunessDTO);
        BGPDriverDAO.instance()
        .submitBGPProcess("Admin", "UNIQUENESS_VIOLATION", "", userPriority,
            new JSONContent(entryData.toJSON()));*/
        
        
        updateTargetBaseEntityforUniquenessBlock(duplicateBaseEntityIIDsAfterEvaluation,ruleExpressionIId, (double) 0);
        insertKpiUniqunessViolationForSourceEntities(duplicateBaseEntityIIDsAfterEvaluation, baseEntityIID, ruleExpressionIId);
        
        
      }
    }
    
  }
  
  private static final String UPDATE_KPI_UNIQUENESS = "Update pxp.baseentitykpirulelink berl set kpi = ?  where"
      + " berl.ruleexpressioniid  =  ? and berl.baseentityiid IN ( ";
  
  @Override
  public void updateTargetBaseEntityforUniquenessBlock(List<Long> targetBaseEntityIIDs, Long ruleExpressionIID, double result) throws RDBMSException{
    
    StringBuilder query = new StringBuilder();
    
    query.append(UPDATE_KPI_UNIQUENESS);
    for(Long targetBaseEntityIID : targetBaseEntityIIDs) {
      query.append(targetBaseEntityIID + ",");
    }
    
    query.deleteCharAt(query.lastIndexOf(","));
    query.append(")");
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> { 
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.setDouble(1, result);
      stmt.setLong(2, ruleExpressionIID);
      stmt.execute();
    });
    
    
  }
  
  public static final String DELETE__KPI = "delete from pxp.baseentitykpirulelink kv where kv.ruleexpressioniid = ? and "
      + "kv.baseentityiid = ? ";
  public void deleteKpiFromKpiRuleLink(long baseEntityIID, long ruleExpressionIId) throws RDBMSException
  {
  RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(DELETE__KPI);
      stmt.setLong(1, ruleExpressionIId);
      stmt.setLong(2, baseEntityIID);
      stmt.execute();
    });
  }

  @Override
  public IRuleDTO refreshDataQualityResult(String ruleCode, IBaseEntityDAO baseEntityDAO, Boolean isAllLocaleIdsApplicable)
      throws RDBMSException
  {
    IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode(ruleCode);
    return applyRules(ruleByCode, localCatalogDAO.getLocaleCatalogDTO().getLocaleID(), isAllLocaleIdsApplicable, baseEntityDAO);
  }
  
  private List<ITagDTO> convertCSElementToTagDTO(ICSELiteralOperand dataQualityRuleExecution) throws CSFormatException
  {
    ICSEList asTagValueList = dataQualityRuleExecution.asTagValueList();
    List<ICSEElement> subElements = asTagValueList.getSubElements();
    List<ITagDTO> tags = new ArrayList<>();
    for(ICSEElement subElement : subElements) {
      ICSETagValue tagValue = (ICSETagValue)subElement;
      tags.add(new TagDTO(tagValue.getCode(), tagValue.getRange()));
    }
    return tags;
  }
  
  private void addUpdatedPropertyRecord(IBaseEntityDAO entityDao, long baseEntityIID,
      List<IPropertyRecordDTO> propertyRecords, IPropertyDTO result,
      ICSELiteralOperand literalOperand, IPropertyRecordDTO propertyRecord,IChangedPropertiesDTO changedProperties) throws CSFormatException
  {
    Set<String> changedAttributeCodes = changedProperties.getAttributeCodes();
    Set<String> changedTagCodes = changedProperties.getTagCodes();
    String propertyCode = result.getCode();
    switch (result.getSuperType()) {
      case ATTRIBUTE:
        if(referencedAttributes.containsKey(propertyCode)) {
          IValueRecordDTO existingValueRecord = (IValueRecordDTO) propertyRecord;
          String value = literalOperand.asString();
          if(existingValueRecord == null) {
            updateOrCreatePropertyRecords(baseEntityIID, propertyRecords, result, existingValueRecord, value,
                (IReferencedSectionAttributeModel) referencedElements.get(propertyCode), referencedAttributes.get(propertyCode),
                changedAttributeCodes);
          }
          else if((existingValueRecord.getProperty().getPropertyType() == PropertyType.TEXT && !existingValueRecord.getValue().equals(value.trim()))
              || (existingValueRecord.getProperty().getPropertyType() == PropertyType.HTML && !existingValueRecord.getAsHTML().equals(value))
              || (existingValueRecord.getProperty().getPropertyType() == PropertyType.MEASUREMENT)
              || (existingValueRecord.getProperty().getPropertyType() == PropertyType.PRICE)) {
            updateOrCreatePropertyRecords(baseEntityIID, propertyRecords, result, existingValueRecord, value,
                (IReferencedSectionAttributeModel) referencedElements.get(propertyCode), referencedAttributes.get(propertyCode),
                changedAttributeCodes);
          }
          else if(existingValueRecord.getProperty().getPropertyType() != PropertyType.TEXT &&  
                  existingValueRecord.getProperty().getPropertyType() != PropertyType.HTML && !existingValueRecord.getValue().equals(value)) {
            updateOrCreatePropertyRecords(baseEntityIID, propertyRecords, result, existingValueRecord, value,
                (IReferencedSectionAttributeModel) referencedElements.get(propertyCode), referencedAttributes.get(propertyCode),
                changedAttributeCodes);
          }
        }
        break;
      
      case TAGS:
        if (referencedTags.containsKey(propertyCode)) {
          ITagsRecordDTO tagsRecord = propertyRecord == null ? entityDao.newTagsRecordDTOBuilder(result).build()
              : (ITagsRecordDTO) propertyRecord;
          
          Set<ITagDTO> selectedTags = tagsRecord.getTags();
          List<ITagDTO> tags = convertCSElementToTagDTO(literalOperand);
          Boolean tagsToBeAdded = false;
          
          if (selectedTags.size() != tags.size()) {
            tagsToBeAdded = true;
          }
          else {
            for (ITagDTO tag : tags) {
              if (!selectedTags.contains(tag)) {
                tagsToBeAdded = true;
                break;
              }
            }
          }
          if (tagsToBeAdded) {
            changedTagCodes.add(propertyCode);
            tagsRecord.setTags(tags.toArray(new ITagDTO[0]));
            propertyRecords.add(tagsRecord);
          }
        }
        break;
      default:
        break;
    }
  }

  private void updateOrCreatePropertyRecords(long baseEntityIID, List<IPropertyRecordDTO> propertyRecords, IPropertyDTO result,
      IValueRecordDTO existingValueRecord, String value, IReferencedSectionAttributeModel referencedElement, IAttribute referencedAttribute,
      Set<String> changedAttributeCodes) throws CSFormatException
  {
    boolean isNumber = false;
    double number = 0.0;
    if(! (result.getPropertyType() == PropertyType.HTML || result.getPropertyType() == PropertyType.TEXT)) {
      isNumber = NumberUtils.isDigits(value);
      number = isNumber  ? Double.parseDouble(value): 0.0;
    }
    String unitSymbol = existingValueRecord == null ? "" : existingValueRecord.getUnitSymbol();
    
    long valueIID = existingValueRecord == null ? 0l : existingValueRecord.getValueIID();
    String localeID = "";
    if(existingValueRecord != null) {
      localeID = existingValueRecord.getLocaleID();
    }
    else if(referencedAttribute.getIsTranslatable()) {
      localeID = localCatalogDAO.getLocaleCatalogDTO().getLocaleID();
    }
    ValueRecordDTO updatedRecord  = null;
    
    if(result.getPropertyType() == PropertyType.TEXT) {
      updatedRecord = new ValueRecordDTO(baseEntityIID, valueIID, result,
          CouplingType.NONE, RecordStatus.DIRECT, value, "", number, unitSymbol, localeID, null);
    }
    else if(result.getPropertyType() == PropertyType.HTML) {
      String text = Jsoup.parse(value).text();
        updatedRecord = new ValueRecordDTO(baseEntityIID, valueIID, result,
            CouplingType.NONE, RecordStatus.DIRECT, text, value, number, unitSymbol, localeID, null);
      
    }
    else if (result.getPropertyType() == PropertyType.MEASUREMENT || result.getPropertyType() == PropertyType.PRICE) {
      unitSymbol = referencedElement.getDefaultUnit() != null ? referencedElement.getDefaultUnit()
          : ((IUnitAttribute) referencedAttribute).getDefaultUnit();
      updatedRecord = new ValueRecordDTO(baseEntityIID, valueIID, result, CouplingType.NONE, RecordStatus.DIRECT, value, "", number,
          unitSymbol, localeID, null);
    }
    else {
      updatedRecord = new ValueRecordDTO(baseEntityIID, valueIID, result,
          CouplingType.NONE, RecordStatus.DIRECT, value, "", number, unitSymbol, localeID, null);
    }
    changedAttributeCodes.add(result.getCode());
    propertyRecords.add(updatedRecord);
  }
  
  private Map<String, IPropertyRecordDTO> getMapOfPropertyRecords(Set<IPropertyRecordDTO> propertyRecords)
  {
    return propertyRecords.stream()
        .collect(Collectors.toMap(propertyRecord -> propertyRecord.getProperty()
            .getCode(), Function.identity()));
  }
  
  private Set<IPropertyRecordDTO> getPropertyRecords(IBaseEntityDAO baseEntityDAO,
      IPropertyDTO[] properties, String localeId, Collection<String> ruleLocaleIds) throws RDBMSException
  {
    Set<IPropertyRecordDTO> propertyRecords = new HashSet<>();
    long baseEntityIID = baseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    
    for (IPropertyDTO property : properties) {
      if (property.getSuperType().equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
        RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          StringBuilder query = new StringBuilder(Q_GET_ALL_VALUE_RECORDS);
          if (!ruleLocaleIds.isEmpty()) {
            query.append(String.format(Q_WITH_LOCALE_ID, localeId));
          }
          
          PreparedStatement statement = currentConn.prepareStatement(query.toString());
          statement.setLong(1, property.getIID());
          statement.setLong(2, baseEntityIID);
          IResultSetParser result = currentConn.getResultSetParser(statement.executeQuery());
          while (result.next()) {
            propertyRecords.add(new ValueRecordDTO(result, property));
          }
        });
      }
      else {
        RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement query = currentConn.prepareStatement(Q_GET_ALL_TAG_RECORDS);
          query.setLong(1, property.getIID());
          query.setLong(2, baseEntityIID);
          IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
          while (result.next()) {
            propertyRecords.add(new TagsRecordDTO(result, property));
          }
        });
      }
    }
    return propertyRecords;
  }
  
  private void propagateNormalization(Map<IRootConfigDTO, ICSELiteralOperand> dataQualityResult,
      IBaseEntityDAO entityDao, String localeId, Collection<String> ruleLocaleIds, IChangedPropertiesDTO changedProperties) throws RDBMSException, CSFormatException
  {
    if (ruleLocaleIds.contains(localeId) || ruleLocaleIds.isEmpty()) {
      IPropertyDTO[] properties = (IPropertyDTO[]) dataQualityResult.keySet()
          .stream()
          .filter(x -> x instanceof IPropertyDTO)
          .collect(Collectors.collectingAndThen(Collectors.toSet(), set -> set.toArray(new IPropertyDTO[0])));
      /*if(properties.length == 0) {
        return;
      }*/

      long baseEntityIID = entityDao.getBaseEntityDTO().getBaseEntityIID();
      List<IPropertyRecordDTO> updatedPropertyRecords = new ArrayList<>();
      List<IPropertyRecordDTO> createdPropertyRecord = new ArrayList<>();
      ILocaleCatalogDAO localeCatalog = entityDao.getLocaleCatalog();
      Set<IPropertyRecordDTO> propertyRecordDTOS = localeCatalog.getPropertyRecordsForEntities(Set.of(baseEntityIID), properties).computeIfAbsent(baseEntityIID, k -> new HashSet<>());
      Map<String, IPropertyRecordDTO> propertyRecords = getMapOfPropertyRecords(propertyRecordDTOS);
    
    for (Entry<IRootConfigDTO, ICSELiteralOperand> result : dataQualityResult.entrySet()) {
      if(result.getKey() instanceof IPropertyDTO){
          
      IPropertyRecordDTO propertyRecord = propertyRecords.get(result.getKey().getCode());
        
        if (propertyRecord != null) {
          addUpdatedPropertyRecord(entityDao, baseEntityIID, updatedPropertyRecords,
              (IPropertyDTO) result.getKey(), result.getValue(), propertyRecord, changedProperties);
        }
        else {
          addUpdatedPropertyRecord(entityDao, baseEntityIID, createdPropertyRecord,
              (IPropertyDTO) result.getKey(), result.getValue(), propertyRecord, changedProperties);
        }
      }
    }
    if(!createdPropertyRecord.isEmpty()) {
      entityDao.createPropertyRecords(createdPropertyRecord.toArray(new IPropertyRecordDTO[0]));
    }
    if(!updatedPropertyRecords.isEmpty()) {
      entityDao.updatePropertyRecords(updatedPropertyRecords.toArray(new IPropertyRecordDTO[0]));
    }
    }

  }
  
  public Set<IClassifierDTO> getClassifiersToAdd(String ruleCode, long baseEntityIID, List<String> baseEntityClassifiersAndTaxonomies) throws RDBMSException, CSFormatException
  {
    
    Set<IClassifierDTO> classifiersToAdd = new HashSet<IClassifierDTO>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IConfigRuleDTO rule = ConfigurationDAO.instance().getRuleByCode(ruleCode);
      Collection<IRuleExpressionDTO> ruleExpressions = rule.getRuleExpressions();
      Iterator<IRuleExpressionDTO> ruleExpressionsIterator = ruleExpressions.iterator();
      while (ruleExpressionsIterator.hasNext()) {
        IRuleExpressionDTO ruleExpressionDTO = ruleExpressionsIterator.next();
        String ruleExpression = ruleExpressionDTO.getRuleExpression();
        
        ICSERule parseRule = (new CSEParser()).parseRule(ruleExpression);
        long ruleExpressionIId = ruleExpressionDTO.getRuleExpressionIId();
        
        ICSEScope scope = parseRule.getScope();
        ICSEEntityFilterNode entityFilter = scope.getEntityFilter();
        Collection<String> includingClassifiers = entityFilter.getIncludingClassifiers();
        
        Collection<String> catalogCodes = ruleExpressionDTO.getCatalogCodes();
        Collection<String> localeIds = ruleExpressionDTO.getLocaleIDs();
        Collection<String> organizationCodes = ruleExpressionDTO.getOrganizationCodes();
        RuleResolver ruleResolver = new RuleResolver(currentConn, localCatalogDAO, catalogCodes, localeIds, organizationCodes,
            new StringBuilder(), includingClassifiers, referencedElements);
        
        Map<IRootConfigDTO, ICSELiteralOperand> dataQualityResult = ruleResolver.evaluateRuleEffects(parseRule.getActionList(),
            ruleExpressionIId, baseEntityIID);
        
        for (Entry<IRootConfigDTO, ICSELiteralOperand> result : dataQualityResult.entrySet()) {
          if (result.getKey() instanceof IClassifierDTO) {
            IClassifierDTO classifierDto = (IClassifierDTO) result.getKey();
            if(!baseEntityClassifiersAndTaxonomies.contains(classifierDto.getClassifierCode()))
              classifiersToAdd.add(classifierDto);
          }
        }
      }
    });
    return classifiersToAdd;
    
  }
  
  private static final String SINGLE_ENTITY_VIOLATIONS = "select qualityflag, message, propertyiid, ruleCode, "
      + "beqr.ruleexpressioniid from pxp.baseentityqualityrulelink beqr left JOIN pxp.ruleexpression re on "
      + "re.ruleexpressioniid = beqr.ruleexpressioniid where baseentityiid = ? and "
      + "(localeids ='{}' or ((? = ANY(localeids) or localeids ISNULL) and localeid = ?) or localeid = 'NAL') and "
      + "(catalogcodes='{}' or ? = ANY(catalogcodes) or catalogcodes ISNULL) and "
      + "(organizationcodes='{}' or ? = ANY(organizationcodes) or organizationcodes ISNULL)";
  
  public Set<IRuleViolationDTO> loadViolations(Long baseEntityIID) throws RDBMSException
  {
    Set<IRuleViolationDTO> violations = new HashSet<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
           
          PreparedStatement stmt = currentConn.prepareStatement(SINGLE_ENTITY_VIOLATIONS);
          stmt.setLong(1, baseEntityIID);
          stmt.setString(2, localCatalogDAO.getLocaleCatalogDTO().getLocaleID());
          stmt.setString(3, localCatalogDAO.getLocaleCatalogDTO().getLocaleID());
          stmt.setString(4, localCatalogDAO.getLocaleCatalogDTO().getCode());
          stmt.setString(5, localCatalogDAO.getLocaleCatalogDTO().getOrganizationCode());
          stmt.execute();
          
          IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
          while (resultSet.next()) {
            violations.add(new RuleViolationDTO(resultSet));
          }
        });
    
    return violations;
  }
  
  private static final String MULTIPLE_ENTITY_VIOLATIONS = "select baseentityiid,qualityflag, message, propertyiid, ruleCode, "
      + "beqr.ruleexpressioniid from pxp.baseentityqualityrulelink beqr left JOIN pxp.ruleexpression re on "
      + "re.ruleexpressioniid = beqr.ruleexpressioniid where baseentityiid IN (%s) and "
      + "(localeids ='{}' or ((? = ANY(localeids) or localeids ISNULL) and localeid = ?) or localeid = 'NAL') and "
      + "(catalogcodes='{}' or ? = ANY(catalogcodes) or catalogcodes ISNULL) and "
      + "(organizationcodes='{}' or ? = ANY(organizationcodes) or organizationcodes ISNULL)";
  
  public Map<Long,Set<IRuleViolationDTO>> loadViolations(Set<Long> baseEntityIIDs) throws RDBMSException
  {
    Map<Long,Set<IRuleViolationDTO>> violations = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      String entityIIds = Text.join(",", baseEntityIIDs);
      String finalQuery = String.format(MULTIPLE_ENTITY_VIOLATIONS, entityIIds);
          PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
          stmt.setString(1, localCatalogDAO.getLocaleCatalogDTO().getLocaleID());
          stmt.setString(2, localCatalogDAO.getLocaleCatalogDTO().getLocaleID());
          stmt.setString(3, localCatalogDAO.getLocaleCatalogDTO().getCode());
          stmt.setString(4, localCatalogDAO.getLocaleCatalogDTO().getOrganizationCode());
          stmt.execute();
          
          IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
          while (resultSet.next()) {
            long baseentityiid = resultSet.getLong("baseentityiid");
            Set<IRuleViolationDTO> current = violations.computeIfAbsent(baseentityiid, k -> new HashSet<>());
            current.add(new RuleViolationDTO(resultSet));
          }
        });
    
    return violations;
  }
  
  private static final String SINGLE_ENTITY_KPI = "select re.rulecode, AVG(berl.kpi) as kpi from pxp.baseentitykpirulelink berl " + 
      "join pxp.ruleexpression re on re.ruleexpressioniid = berl.ruleexpressioniid where baseentityiid = ? and localeid = ? " + 
      "group by re.rulecode";
  public List<IKPIResultDTO> loadKPI(Long baseEntityIID, String localeID) throws RDBMSException
  {
    List<IKPIResultDTO> kpiResults = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          
          PreparedStatement stmt = currentConn.prepareStatement(SINGLE_ENTITY_KPI);
          stmt.setLong(1, baseEntityIID);
          stmt.setString(2, localeID);
          stmt.execute();
          
          IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
          while(resultSet.next()) {
            kpiResults.add(new KPIResultDTO(resultSet));
          }
        });
    
    return kpiResults;
  }

  public void evaluateProductIdentifier(long propertyIID, long baseEntityIId, long classifierIID)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          
          RuleCatalogDAS ruleCatalogDAS = new RuleCatalogDAS(currentConn);
          
          IClassifierDTO classifier = ConfigurationDAO.instance()
              .getClassifierByIID(classifierIID);
          IPropertyDTO property = ConfigurationDAO.instance()
              .getPropertyByIID(propertyIID);

      ILocaleCatalogDTO catalog = this.getCatalog();
      String ruleExpression = "for $ctlg = " + catalog.getCatalogCode() + " $entity is [c>" + classifier.getCode() + "] when unique([ " + property
          .getCode() + " ]) ";
      ICSERule parseRule = (new CSEParser()).parseRule(ruleExpression);

      RuleResolver ruleResolver = new RuleResolver(currentConn, localCatalogDAO, List.of(catalog.getCatalogCode()), List.of(catalog.getLocaleID()),
          List.of(catalog.getOrganizationCode()), new StringBuilder(), null, referencedElements);
      ICSELiteralOperand result = ruleResolver.getResult(baseEntityIId, parseRule.getEvaluation());

      ruleCatalogDAS.deleteViolation(currentConn, "", classifierIID, baseEntityIId);
      if (!result.asBoolean()) {
        List<Long> violatedEntities = ruleCatalogDAS.isProductIdentifierUnique(propertyIID, baseEntityIId, classifierIID, getCatalog());
        if (violatedEntities.size() > 1) {
          ruleCatalogDAS.insertIdentifierViolation(classifierIID, null, baseEntityIId, propertyIID,
              "Value not unique. Please enter a unique value.");
        }
      }
        });
  }

  public static final String UNIQUNESS_BLOCK = "select berl.kpi, re.rulecode from pxp.baseentitykpirulelink berl JOIN "
      + "pxp.ruleexpression re ON re.ruleexpressioniid = berl.ruleexpressioniid where berl.baseentityiid = ? "
      + "and re.rulecode IN ( ";
  
  @Override
  public List<IKPIResultDTO> loadKPIForUniqunessBlock(List<String> ruleCodes, Long baseEntityIID) throws RDBMSException
  {
    StringBuilder query = new StringBuilder();
    List<IKPIResultDTO> kpiResults = new ArrayList<>();

    query.append(UNIQUNESS_BLOCK);
    
    for(String ruleCode : ruleCodes) {
      query.append( "\'" + ruleCode + "\'" + ",");
    }
    
    query.deleteCharAt(query.lastIndexOf(","));
    query.append(" ) group by re.rulecode, berl.kpi");
    
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.setLong(1, baseEntityIID);
      stmt.execute();
      
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while(resultSet.next()) {
        kpiResults.add(new KPIResultDTO(resultSet));
      }
    });
    
    return kpiResults;
  }

  public static final String GET_BASE_ENTITY_IID_FOR_UNIQUNESS = "select berl.baseentityiid"
      + "from pxp.baseentitykpirulelink berl join pxp.ruleexpression re ON re.ruleexpressioniid = berl.ruleexpressioniid "
      + "join pxp.baseentity be on berl.baseentityiid = be.baseentityiid and be.ismerged != true where (re.catalogCodes @> ? or array_length(re.catalogCodes , 1) = 0 )"
      + " and re.rulecode IN ( ";
  
  
  public static final String DELETE_QUERY = "delete from pxp.baseentitykpirulelink berl where berl.baseentityiid = ?";
  
  @Override
  public void deleteKpiOnBaseEntityDelete(long baseEntityiid) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_QUERY);
      stmt.setLong(1, baseEntityiid);
      stmt.execute();
      
    });
  }

  public static final String DELETE__KPI_VIOLATION = "delete from pxp.kpiuniquenessviolation kv where kv.ruleexpressioniid = ? and "
      + "(kv.sourceiid = ? OR kv.targetiid = ?)";
  @Override
  public void deleteKpiFromKpiUniquenessViolation(long sourceIID, List<Long> targetIIDs, long ruleExpressionIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(DELETE__KPI_VIOLATION);
      stmt.setLong(1, ruleExpressionIID);
      stmt.setLong(2, sourceIID);
      stmt.setLong(3, sourceIID);
      stmt.execute();
      
    });
  }
  
  public static final String INSERT_KPI_VIOLATION = "INSERT INTO pxp.kpiuniquenessviolation(ruleexpressioniid, "
      + "sourceiid, targetiid) VALUES ";
  @Override
  public void insertKpiUniqunessViolation(long sourceIID, List<Long> targetIIDs, long ruleExpressionIID) throws RDBMSException
  {
    StringBuilder query = new StringBuilder();
    
    query.append(INSERT_KPI_VIOLATION);
    
    for(Long targetIID : targetIIDs) {
      query.append(" (" +ruleExpressionIID + "," +sourceIID + "," +targetIID + "),");
    }
    
    query.deleteCharAt(query.lastIndexOf(","));
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }

  
  
  @Override
  public void insertKpiUniqunessViolationForSourceEntities(List<Long> sourceIIDs, long targetIID, long ruleExpressionIID)
      throws RDBMSException
  {
    StringBuilder query = new StringBuilder();
    
    query.append(INSERT_KPI_VIOLATION);
    
    for (Long sourceIID : sourceIIDs) {
      query.append(" (" + ruleExpressionIID + "," + sourceIID + "," + targetIID + "),");
    }
    
    query.deleteCharAt(query.lastIndexOf(","));
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
    
  }
 
  public static final String GET_ALL_TARGET_ENTITIES = "select kv.ruleexpressioniid, kv.targetiid from "
      + "pxp.kpiuniquenessviolation kv where sourceiid = ?";
  
  @Override
  public Map<Long, List<Long>> getAllTargetEntitiesForKpiUniqueness(long sourceEntityIID) throws RDBMSException
  {
    Map<Long, List<Long>> kpiUniquenessDTOs = new HashMap<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(GET_ALL_TARGET_ENTITIES);
      stmt.setLong(1, sourceEntityIID);
      stmt.execute();
      
      ResultSet resultSet = stmt.getResultSet();
      
      while(resultSet.next()) {
        if(kpiUniquenessDTOs.containsKey(resultSet.getLong("ruleexpressioniid"))) {
          List<Long> targetEntitiesIIDs = kpiUniquenessDTOs.get(resultSet.getLong("ruleexpressioniid"));
          targetEntitiesIIDs.add(resultSet.getLong("targetiid"));
        }else {
          List<Long> targetEntitesIIds = new ArrayList<>();
          targetEntitesIIds.add(resultSet.getLong("targetiid"));
          kpiUniquenessDTOs.put(resultSet.getLong("ruleexpressioniid"), targetEntitesIIds);
        }
      }
    });
    
    return kpiUniquenessDTOs;
  }

  public static final String DELETE_FROM_KPI = "delete from pxp.kpiuniquenessviolation kv where (kv.sourceiid = ? OR kv.targetiid = ?)";
  
  @Override
  public void deleteFromKpiUniquenessViolation(long entityIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(DELETE_FROM_KPI);
      stmt.setLong(1, entityIID);
      stmt.setLong(2, entityIID);
      stmt.execute();
    });
  }
  
  public static final String GET_UNIQUENESS_DASHBOARD_RESULT = "select distinct berl.baseentityiid, min(berl.kpi) AS kpi, re.rulecode "
      + "from pxp.baseentitykpirulelink berl "
      + "JOIN pxp.ruleexpression re ON re.ruleexpressioniid =  berl.ruleexpressioniid "
      + "LEFT JOIN pxp.baseentity be ON be.baseentityiid = berl.baseentityiid and be.ismerged != true "
      + "Left JOIN pxp.baseentityclassifierlink becl ON becl.baseentityiid = berl.baseentityiid "
      + "JOIN pxp.classifierconfig cc ON cc.classifieriid = be.classifieriid OR cc.classifieriid = becl.otherclassifieriid "
      + "LEFT JOIN (SELECT entityiid , unnest(akeys(usrtags)) as tagvaluecode, "
      + "unnest(avals(usrtags)) as relevance FROM pxp.tagsrecord)  resultset "
      + "ON be.baseentityiid = resultset.entityiid "
      + "where ruleCode = \'%s\' and localeid = \'%s\' and catalogCode = \'%s\' and organizationCode = \'%s\' ";
  
  @Override
  public IRuleResultDTO loadKpiForUniqueness(String ruleCode, String entityCode, String type, 
      List<String> classifierInfo, List<String> tagInfo) throws RDBMSException
  {
    
    RuleResultDTO ruleResult = new RuleResultDTO(RuleType.kpi, ruleCode);
    ILocaleCatalogDTO localCatDTO = localCatalogDAO.getLocaleCatalogDTO();
    
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    
    StringBuilder queryForClassifier = new StringBuilder();
    
    if(!type.equals("tag")) {
      
      IClassifierDTO classifierDTO = configurationDAO.getClassifierByCode(entityCode);
      
      ClassifierType classifierType = classifierDTO.getClassifierType();
      if(classifierType == ClassifierType.CLASS) {
        queryForClassifier.append("and classifiercode = '"+entityCode+"'");
      }else {
        queryForClassifier.append("and hierarchyiids && '{\" " +classifierDTO.getClassifierIID()+ "\"}' ");
      }
    }
    
    if(!classifierInfo.isEmpty() && queryForClassifier.length() == 0) {
      
      String pathClassifierCode = classifierInfo.get(classifierInfo.size() - 1);
      
      IClassifierDTO classifierDTO = configurationDAO.getClassifierByCode(pathClassifierCode);
      
      ClassifierType classifierType = classifierDTO.getClassifierType();
      if (classifierType == ClassifierType.CLASS) {
        queryForClassifier.append("and classifiercode = '" + pathClassifierCode + "'");
      }
      else {
        queryForClassifier.append("and hierarchyiids && '{\" " + classifierDTO.getClassifierIID() + "\"}' ");
      }      
    }
    
    StringBuilder finalQuery = new StringBuilder();
    
    Iterator<String> iterator = tagInfo.iterator();
    while(iterator.hasNext()) {
      finalQuery.append("select avg(kpi) AS kpi, baseentityiid, rulecode from ( ");
      finalQuery.append(String.format(GET_UNIQUENESS_DASHBOARD_RESULT, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
      finalQuery.append(queryForClassifier);
      finalQuery.append("and tagvaluecode = '" +iterator.next() +"' ");
      if(iterator.hasNext()) {
        finalQuery.append(" group by rulecode, berl.baseentityiid, cc.classifiercode, resultset.tagvaluecode");
        finalQuery.append(") AS SUBQUERY GROUP BY rulecode, baseentityiid");
        finalQuery.append(" intersect ");
      }
    }
    
    if(!tagInfo.isEmpty()) {
      
      if(type.equals("tag")) {
        finalQuery.append(" group by rulecode, berl.baseentityiid, cc.classifiercode, resultset.tagvaluecode");
        finalQuery.append(") AS SUBQUERY GROUP BY rulecode, baseentityiid");
        finalQuery.append(" intersect ");
        
        finalQuery.append("select avg(kpi) AS kpi, baseentityiid, rulecode from ( ");
        finalQuery.append(String.format(GET_UNIQUENESS_DASHBOARD_RESULT, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        finalQuery.append("and tagvaluecode = '" +entityCode +"' ");
      } 
      finalQuery.append(" group by rulecode, berl.baseentityiid, cc.classifiercode, resultset.tagvaluecode");
      finalQuery.append(") AS SUBQUERY GROUP BY rulecode, baseentityiid");
      
      finalQuery.insert(0, "select rulecode, avg(kpi) AS kpi from(");
      finalQuery.append(") AS SUBQUERY GROUP BY rulecode");
    }else {
      
      if(type.equals("tag")) {
        finalQuery.append("select avg(kpi) AS kpi, count(baseentityiid) AS totalcount, rulecode from ( ");
        finalQuery.append(String.format(GET_UNIQUENESS_DASHBOARD_RESULT, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        finalQuery.append("and tagvaluecode = '" +entityCode +"' ");
        finalQuery.append(" group by berl.baseentityiid, re.rulecode, cc.classifiercode, resultset.tagvaluecode");
        finalQuery.append(") AS SUBQUERY GROUP BY rulecode ");
      }else {
        
        finalQuery.append("select avg(kpi) AS kpi, count(baseentityiid) AS totalcount, rulecode from ( ");
        finalQuery.append(String.format(GET_UNIQUENESS_DASHBOARD_RESULT, ruleCode, localCatDTO.getLocaleID(), localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
        finalQuery.append(queryForClassifier);
        finalQuery.append(" group by berl.baseentityiid, re.rulecode, cc.classifiercode ");
        finalQuery.append(") AS SUBQUERY GROUP BY rulecode ");
      }
    }

    
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery.toString());
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());

      while (result.next()) {
        Double kpi = result.getDouble("kpi");
        //long totalNumberOfContents = result.getInt("totalCount");
        ruleResult.fillKPI( kpi, 0l);
      }
    });
    return ruleResult;
  }
  
  private final String GET_CONTENTS_WITH_RULECODES = "SELECT DISTINCT b.baseentityiid FROM pxp.ruleexpression AS re JOIN "
      + "pxp.baseentityqualityrulelink AS b ON re.ruleexpressioniid = b.ruleexpressioniid "
      + "join pxp.baseentity be on b.baseentityiid = be.baseentityiid and be.ismerged != true WHERE re.rulecode IN (%s)";
  
  @Override
  public List<Long> getViolatedEntitiesOfRuleCodes(Set<String> dataRuleCodes) throws RDBMSException{
   
    List<Long> baseEntityIIDs = new ArrayList<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String ruleCodes = Text.join(",", dataRuleCodes, "'%s'");
      PreparedStatement query = currentConn.prepareStatement(String.format(GET_CONTENTS_WITH_RULECODES, ruleCodes));
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while(result.next()) {
        baseEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });
    
    return baseEntityIIDs;
  }
  
  private final String GET_LAST_EVALUATED = "SELECT max(lastevaluated) as lastevaluated from pxp.baseentitykpirulelink  where baseentityiid = ?";
  
  public Long getLastEvaluatedKpiForBaseEntity(Long baseEntityIid) throws RDBMSException
  {
    AtomicLong at = new AtomicLong();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(GET_LAST_EVALUATED);
      query.setLong(1, baseEntityIid);
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      if (result.next()) {
        long lastEvaluated = result.getLong("lastevaluated");
        at.set(lastEvaluated);
      }
    });
    return at.get();
  }
  
}