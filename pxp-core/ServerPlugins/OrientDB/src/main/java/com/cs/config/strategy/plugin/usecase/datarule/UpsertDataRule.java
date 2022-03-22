package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.SaveDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IAttributeValueNormalization;
import com.cs.core.config.interactor.entity.datarule.IConcatenatedNormalization;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.validationontype.InvalidDataRuleTypeException;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.config.interactor.model.datarule.IModifiedRuleEntityModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.plugin.utility.property.tag.TagDbUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.cs.config.strategy.plugin.usecase.datarule.UpsertDataRule.RuleContestants.*;

public class UpsertDataRule extends AbstractOrientPlugin {

  private IExceptionModel failure = new ExceptionModel();

  private static final String       LANGUAGES              = "languages";
  private static final String       ORGANIZATION           = "organizations";
  private static final String       PHYSICAL_CATALOG_IDS   = "physicalCatalogIds";
  private static final String       TAGS                   = "tags";
  private static final String       ATTRIBUTES             = "attributes";
  private static final String       KLASSES                = "types";
  private static final String       TAXONOMIES             = "taxonomies";
  private static final String       RELATIONSHIPS          = "relationships";
  private static final String       SUCCESS_DATA_RULE_LIST = "successDataRuleList";

  private static final List<String> RULE_TYPE_LIST         = Arrays.asList(Constants.CLASSIFICATION,
      Constants.STANDARDIZATION_AND_NORMALIZATION, Constants.VIOLATION);

  private List<String>              fieldsToExclude        = Arrays.asList(ORGANIZATION, PHYSICAL_CATALOG_IDS, TAGS, ATTRIBUTES, KLASSES,
      TAXONOMIES, RELATIONSHIPS, IAttributeValueNormalization.VALUE_ATTRIBUTE_ID, IConcatenatedNormalization.ATTRIBUTE_CONCATENATED_LIST,
      ConfigTag.normalizations.toString(), ConfigTag.ruleViolations.toString());

  enum RuleContestants {
    numeric, literal, date, tag;
  }

  enum RuleType {
    regex(List.of(literal)), empty(List.of(numeric, literal, date, tag)), notempty(List.of(numeric, literal, date, tag)), exact(
        List.of(numeric, literal, date, tag)), contains(List.of(numeric, literal, tag)), lt(List.of(numeric, date)), gt(
        List.of(numeric, date)), range(List.of(numeric, date)), start(List.of(numeric, literal)), end(List.of(numeric, literal)), length_lt(
        List.of(literal)), length_gt(List.of(literal)), length_equal(List.of(literal)), length_range(List.of(literal));

    private List<RuleContestants> ruleContestants;

    private RuleType(List<RuleContestants> ruleContestants)
    {
      this.ruleContestants = ruleContestants;
    }

  }

  private static final List<RuleType> notAttributeLinkedType = List.of(RuleType.regex, RuleType.empty, RuleType.notempty, RuleType.range,
      RuleType.length_range);

  enum TransformationType {
    concat, value, attributeValue, propercase, uppercase, lowercase, replace, substring, trim;
    private static final List<TransformationType> NUMERIC_TRANSFORMATION_TYPE = List.of( value, attributeValue);
  }

  public UpsertDataRule(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpsertDataRule/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> dataRuleList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> successDataRuleList = new ArrayList<>();
    List<Map<String, Object>> failedTDataRuleList = new ArrayList<>();

    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.DATA_RULES, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> dataRule : dataRuleList) {
      try {
        Map<String, Object> dataRuleMap = upsertDataRule(dataRule, vertexType);
        Map<String, Object> createdDataRuleMap = new HashMap<>();
        createdDataRuleMap.put(CommonConstants.CODE_PROPERTY, dataRuleMap.get(CommonConstants.CODE_PROPERTY));
        createdDataRuleMap.put(CommonConstants.LABEL_PROPERTY, dataRuleMap.get(CommonConstants.LABEL_PROPERTY));
        successDataRuleList.add(createdDataRuleMap);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) dataRule.get(CommonConstants.LABEL_PROPERTY));
        addToFailureIds(failedTDataRuleList, dataRule);
      }
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, successDataRuleList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTDataRuleList);
    
    List<Map<String, Object>> prepareResponseMap = prepareResponseMap(successDataRuleList);
    result.put(SUCCESS_DATA_RULE_LIST, prepareResponseMap);
    return result;
  }
  
  private Map<String, Object> upsertDataRule(Map<String, Object> importData, OrientVertexType vertexType) throws Exception
  {

    String entityCode = (String) importData.get(CommonConstants.CODE_PROPERTY);
    Vertex dataRuleNode;
    try {
      dataRuleNode = UtilClass.getVertexByCode(entityCode, VertexLabelConstants.DATA_RULE);
    }
    catch (NotFoundException e) {
      Map<String, Object> initialDataRuleFields = prepareDataRuleMap(importData);
      dataRuleNode = UtilClass.createNode(initialDataRuleFields, vertexType, fieldsToExclude);
    }

    String ruleType = handleRuleType(importData, dataRuleNode);

    handleLanguages(importData, entityCode, dataRuleNode, ruleType);

    handlePhysicalCatalogs(importData, dataRuleNode);

    handleOrganizations(importData, dataRuleNode);

    handleCause(importData, dataRuleNode, ruleType);

    handleEffect(importData, dataRuleNode, ruleType);

    UtilClass.getGraph().commit();

    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.put(CommonConstants.CODE_PROPERTY, dataRuleNode.getProperty(CommonConstants.CODE_PROPERTY));
    returnMap.put(CommonConstants.LABEL_PROPERTY, dataRuleNode.getProperty(CommonConstants.LABEL_PROPERTY));
    return returnMap;
  }

  private void handleEffect(Map<String, Object> importData, Vertex dataRuleNode, String ruleType) throws Exception
  {
    Boolean isLanguageDependant = (Boolean) dataRuleNode.getProperty(IDataRule.IS_LANGUAGE_DEPENDENT);
    if (ruleType.equals(Constants.VIOLATION)) {
      List<Map<String, Object>> ruleViolations = (List<Map<String, Object>>) importData.get(IDataRule.RULE_VIOLATIONS);
      handleRuleViolations(dataRuleNode, ruleViolations, isLanguageDependant);
    }
    else {
      List<Map<String, Object>> normalizations = (List<Map<String, Object>>) importData.get(IDataRule.NORMALIZATIONS);
      handleNormalizations(dataRuleNode, normalizations,isLanguageDependant,ruleType) ;
    }
  }

  private void handleNormalizations(Vertex dataRule, List<Map<String, Object>> normalizationToImport, boolean isLanguageDependant,
      String ruleType) throws Exception
  {
    String code = dataRule.getProperty(CommonConstants.CODE_PROPERTY);
    Map<String, Object> importNormalization = validateAndModelizeNormalization(normalizationToImport, ruleType, isLanguageDependant, code);
    Map<String, Object> existingNormalizations = GetDataRuleUtils.getNormalizations(dataRule);
    List<String> currentNormalizations = new ArrayList<>(existingNormalizations.keySet());

    List<Map<String, Object>> addedNormalizations = new ArrayList<>();
    List<Map<String, Object>> modifiedNormalizations = new ArrayList<>();

    for (String normalizationEntityId : importNormalization.keySet()) {
      Map<String, Object> normalization = (Map<String, Object>) importNormalization.get(normalizationEntityId);
      if (existingNormalizations.containsKey(normalizationEntityId)) {
        currentNormalizations.remove(normalizationEntityId);
        Map<String, Object> modifiedNormalization = (Map<String, Object>) normalization;
        Map<String, Object> existingNormalization = (Map<String, Object>) existingNormalizations.get(normalizationEntityId);
        modifiedNormalization.put(CommonConstants.ID_PROPERTY, existingNormalization.get(CommonConstants.ID_PROPERTY));
        modifiedNormalizations.add(modifiedNormalization);
      }
      else {
        normalization.put(ConfigTag.baseType.toString(), CommonConstants.NORMALIZATION_BASE_TYPE);
        addedNormalizations.add((Map<String, Object>) normalization);
      }
    }

    List<String> normalizationsToDelete = new ArrayList<>();
    for(String normalizationToDelete : currentNormalizations){
      Map<String, Object> normalization = (Map<String, Object>) existingNormalizations.get(normalizationToDelete);
      normalizationsToDelete.add((String) normalization.get(CommonConstants.ID_PROPERTY));
    }
    SaveDataRuleUtils.handleAddedNormalizations(dataRule, addedNormalizations);
    SaveDataRuleUtils.handleDeletedNormalizations(normalizationsToDelete);
    SaveDataRuleUtils.handleModifiedNormalizations(dataRule, modifiedNormalizations);
  }

  private void handleCause(Map<String, Object> importData, Vertex dataRuleNode, String ruleType) throws Exception
  {
    //for class
    List<String> classesToImport = (List<String>) importData.get(KLASSES);
    handleClassifier(classesToImport, dataRuleNode, RelationshipLabelConstants.HAS_KLASS_RULE_LINK, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);

    //for taxonomy
    List<String> taxonomiesToImport = (List<String>) importData.get(TAXONOMIES);
    handleClassifier(taxonomiesToImport, dataRuleNode, RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK, VertexLabelConstants.ROOT_KLASS_TAXONOMY);

    // for classification no need to attach attributes and tags
    if (!ruleType.equals(Constants.CLASSIFICATION)) {
      handleAttributes(importData, dataRuleNode);
      handleTags(importData, dataRuleNode);
    }
  }

  private void handleTags(Map<String, Object> importData, Vertex dataRule) throws Exception
  {
    List<Map<String, Object>> tagsToImport = (List<Map<String, Object>>) importData.get(CommonConstants.TAGS);
    Map<String, Object> tagRules = GetDataRuleUtils.getTagRules(dataRule);

    Map<String, Object> ADM = prepareADMForPropertyRule(dataRule, tagsToImport, tagRules, VertexLabelConstants.ENTITY_TAG);

    SaveDataRuleUtils.handleAddedTagRules(dataRule, (List<Map<String, Object>>) ADM.get(CommonConstants.ADDED));
    SaveDataRuleUtils.handleDeletedTagRules(dataRule, (List<String>) ADM.get(CommonConstants.DELETED));
    SaveDataRuleUtils.handleModifiedTagRules(dataRule, (List<Map<String, Object>>) ADM.get(CommonConstants.MODIFIED));
  }

  private void handleAttributes(Map<String, Object> importData, Vertex dataRule) throws Exception
  {
    List<Map<String, Object>> attributesToImport = (List<Map<String, Object>>) importData.get(CommonConstants.ATTRIBUTES);
    Map<String, Object> attributesForRules = GetDataRuleUtils.getAttributesForRules(dataRule);

    Map<String, Object> ADM = prepareADMForPropertyRule(dataRule, attributesToImport, attributesForRules, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);

    SaveDataRuleUtils.handleAddedAttributeRules(dataRule, (List<Map<String, Object>>) ADM.get(CommonConstants.ADDED));
    SaveDataRuleUtils.handleDeletedAttributeRules(dataRule, (List<String>) ADM.get(CommonConstants.DELETED));
    SaveDataRuleUtils.handleModifiedAttributeRules(dataRule, (List<Map<String, Object>>) ADM.get(CommonConstants.MODIFIED));
  }

  private Map<String, Object> prepareADMForPropertyRule(Vertex dataRule, List<Map<String, Object>> propertiesToImport,
      Map<String, Object> propertyForRules, String entityType)
  {
    Map<String, Object> ADM = new HashMap<>();
    List<String> existingProperties = new ArrayList<>(propertyForRules.keySet());
    List<Map<String, Object>> propertiesToModify = new ArrayList<>();
    List<Map<String, Object>> propertiesToAdd = new ArrayList<>();

    for (Map<String, Object> propertyToImport : propertiesToImport) {
      try {
        String code = (String) propertyToImport.get(CommonConstants.ENTITY_ID_PROPERTY);

        Vertex vertex = validatePropertyRule(propertyToImport, entityType, dataRule);

        if (existingProperties.contains(code)) {
          existingProperties.remove(code);
          Map<String, Object> existingProperty = (Map<String, Object>) propertyForRules.get(code);
          Map<String, Object> modifiedProperty = prepareADMForRules(propertyToImport, entityType, existingProperty, vertex);
          modifiedProperty.put(CommonConstants.ID_PROPERTY, existingProperty.get(CommonConstants.ID_PROPERTY));
          propertiesToModify.add(modifiedProperty);
        }
        else {
          propertiesToAdd.add(propertyToImport);
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, dataRule.getProperty(CommonConstants.CODE_PROPERTY),
            dataRule.getProperty(CommonConstants.LABEL_PROPERTY));
      }
    }

    ADM.put(CommonConstants.ADDED, propertiesToAdd);
    ADM.put(CommonConstants.MODIFIED, propertiesToModify);
    ADM.put(CommonConstants.DELETED, existingProperties);
    return ADM;
  }

  private Map<String, Object> prepareADMForRules(Map<String, Object> propertiesToImport, String entityType,
      Map<String, Object> existingProperty, Vertex vertex)
  {
    Map<String, Object> modifiedRule = new HashMap<>();
    Map<String, Map<String, Object>> existingRules = (Map<String, Map<String, Object>>) existingProperty.get(CommonConstants.RULES_PROPERTY);
    List<String> existingRuleCodes = new ArrayList<>(existingRules.keySet());
    List<Map<String, Object>> rulesToImport = (List<Map<String, Object>>) propertiesToImport.get(CommonConstants.RULES_PROPERTY);

    List<Map<String, Object>> addedRules = new ArrayList<>();
    List<Map<String, Object>> modifiedRules = new ArrayList<>();

    for (Map<String, Object> rule : rulesToImport) {
      try {
        //validity for appropriate rule in appropriate type
        validateRule(rule, entityType, vertex);

        String code = (String) rule.get(CommonConstants.CODE_PROPERTY);
        if (existingRuleCodes.contains(code)) {
          existingRuleCodes.remove(code);
          rule.put(CommonConstants.ID_PROPERTY, code);
          modifiedRules.add(rule);
        }
        else {
          addedRules.add(rule);
        }
      }
      catch(Exception exception){
        ExceptionUtil.addFailureDetailsToFailureObject(failure, exception, null, null);
      }

    }

    modifiedRule.put(IModifiedRuleEntityModel.ADDED_RULES, addedRules);
    modifiedRule.put(IModifiedRuleEntityModel.MODIFIED_RULES, modifiedRules);
    modifiedRule.put(IModifiedRuleEntityModel.DELETED_RULES, existingRuleCodes);
    return modifiedRule;
  }

  private void handleClassifier(List<String> classifiersToImport, Vertex dataRule, String classifierLink, String classifierType)
      throws Exception
  {

    List<String> validClassifierIds = getValidClassifiersForRule(classifiersToImport, classifierType, dataRule.getProperty(CommonConstants.CODE_PROPERTY));

    Map<String, Edge> classifierInCause = getClassifierInRuleCause(dataRule, classifierLink);

    List<String> existingClassifierIds = new ArrayList<>(classifierInCause.keySet());
    List<String> deletedClassifiers = ListUtils.subtract(existingClassifierIds, validClassifierIds);
    List<String> addedClassifiers = ListUtils.subtract(validClassifierIds, existingClassifierIds);

    for (String classifierId : addedClassifiers) {
      Vertex classifier = UtilClass.getVertexById(classifierId, classifierType);
      classifier.addEdge(classifierLink, dataRule);
    }

    for(String deletedClassifier : deletedClassifiers) {
      classifierInCause.get(deletedClassifier).remove();
    }
  }

  private Map<String, Edge> getClassifierInRuleCause(Vertex dataRuleNode, String classifierLink)
  {
    Map<String, Edge> existingClassifiers = new HashMap<>();
    Iterable<Edge> classifierEdges = dataRuleNode.getEdges(Direction.IN, classifierLink);
    for(Edge classifierEdge : classifierEdges){
      String code = classifierEdge.getVertex(Direction.OUT).getProperty(CommonConstants.CODE_PROPERTY);
      existingClassifiers.put(code, classifierEdge);
    }
    return existingClassifiers;
  }

  private void handlePhysicalCatalogs(Map<String, Object> importData, Vertex dataRuleNode)
  {
    List<String> importCatalogs = (List<String>) importData.get(PHYSICAL_CATALOG_IDS);
    //check validity of catalog codes
    importCatalogs.retainAll(Constants.PHYSICAL_CATALOG_IDS);
    dataRuleNode.setProperty(IDataRule.PHYSICAL_CATALOG_IDS, importCatalogs);
  }

  private String handleRuleType(Map<String, Object> importData, Vertex dataRuleNode) throws InvalidDataRuleTypeException
  {
    String ruleType = (String) importData.get(CommonConstants.TYPE);
    try {
      UtilClass.validateOnType(RULE_TYPE_LIST, ruleType, true);
      String currentRuleType = dataRuleNode.getProperty(IDataRule.TYPE);
      if (StringUtils.isEmpty(currentRuleType)) {
        ruleType = StringUtils.isEmpty(ruleType) ? Constants.CLASSIFICATION: ruleType;
        dataRuleNode.setProperty(CommonConstants.TYPE_PROPERTY, ruleType);
      }
    }
    catch (InvalidTypeException in) {
      throw new InvalidDataRuleTypeException(in);
    }
    return ruleType;
  }

  private void handleLanguages(Map<String, Object> importData, String entityCode, Vertex dataRuleNode, String ruleType) throws Exception
  {
    Boolean isLanguageDependant = dataRuleNode.getProperty(IDataRule.IS_LANGUAGE_DEPENDENT);
    if(isLanguageDependant == null ) {
      isLanguageDependant = (Boolean) importData.get(IDataRule.IS_LANGUAGE_DEPENDENT);
      if(isLanguageDependant == null || ruleType.equals(Constants.CLASSIFICATION)){
        isLanguageDependant = false;
      }
      dataRuleNode.setProperty(IDataRule.IS_LANGUAGE_DEPENDENT, isLanguageDependant);
    }
    if(!isLanguageDependant) {
      return;
    }

    List<String> languagesToImport = (List<String>) importData.get(LANGUAGES);
    List<String> validLanguages = ImportUtils.getValidNodeCodes(languagesToImport, VertexLabelConstants.LANGUAGE, failure, entityCode);
    dataRuleNode.setProperty(IDataRule.LANGUAGES, validLanguages);

    Map<String, Vertex> languagesForDataRules = DataRuleUtils.getLanguagesForDataRules(dataRuleNode);

    List<String> existingLanguages = new ArrayList<>(languagesForDataRules.keySet());
    List<String> deletedLanguages = ListUtils.subtract(existingLanguages, validLanguages);
    List<String> addedLanguages = ListUtils.subtract(validLanguages, existingLanguages);

    DataRuleUtils.addLanguages(dataRuleNode, addedLanguages);
    DataRuleUtils.manageDeletedLanguages(dataRuleNode, deletedLanguages);
  }

  public void handleRuleViolations(Vertex dataRule, List<Map<String, Object>> ruleViolations, Boolean isLanguageDependant) throws Exception
  {
    Map<String, Object> existingViolations = GetDataRuleUtils.getRuleViolationsForRule(dataRule);
    List<String> violationsToDelete = new ArrayList<>(existingViolations.keySet());
    List<Map<String, Object>> modifiedViolations = new ArrayList<>();
    List<Map<String, Object>> addedViolations = new ArrayList<>();

    for (Map<String, Object> ruleViolation : ruleViolations) {
      String entityId = (String) ruleViolation.get(CommonConstants.ENTITY_ID_PROPERTY);
      String type = (String) ruleViolation.get(CommonConstants.TYPE_PROPERTY);
      type = type.toLowerCase();
      String vertexType = getVetexTypeByType(type);
      Vertex node = UtilClass.getVertexByCode(entityId, vertexType);
      try {
        ruleViolation.remove(ConfigTag.entityType.toString());
        type = convertTagsToTag(type);
        ruleViolation.put(CommonConstants.TYPE_PROPERTY, type);
        ruleViolation.remove(ConfigTag.entityAttributeType.toString());
        if(type.equals(CommonConstants.TAG) && Boolean.FALSE.equals(isLanguageDependant)) {
          preapreADMforProperty(existingViolations, violationsToDelete, modifiedViolations, addedViolations, ruleViolation, entityId);
        }else if(type.equals(CommonConstants.ATTRIBUTE)) {
          boolean isTranslatable = node.getProperty(IAttribute.IS_TRANSLATABLE);
          if(Boolean.TRUE.equals(isLanguageDependant) && Boolean.TRUE.equals(isTranslatable)) {
            preapreADMforProperty(existingViolations, violationsToDelete, modifiedViolations, addedViolations, ruleViolation, entityId);
          }else if(Boolean.FALSE.equals(isLanguageDependant) && Boolean.FALSE.equals(isTranslatable)) {
            preapreADMforProperty(existingViolations, violationsToDelete, modifiedViolations, addedViolations, ruleViolation, entityId);
          }else {
            throw new InvalidDataRuleTypeException();
          }
        }else {
          throw new InvalidDataRuleTypeException();
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, entityId, type);
      }
    }
    SaveDataRuleUtils.handleAddedRuleViolations(dataRule, addedViolations);
    SaveDataRuleUtils.handleDeletedRuleViolations(violationsToDelete);
    SaveDataRuleUtils.handleModifiedRuleViolations(dataRule, modifiedViolations);
  }

  private void preapreADMforProperty(Map<String, Object> existingViolations, List<String> violationsToDelete,
      List<Map<String, Object>> modifiedViolations, List<Map<String, Object>> addedViolations, Map<String, Object> ruleViolation,
      String entityId)
  {
    if (existingViolations.containsKey(entityId)) {
      violationsToDelete.remove(entityId);
      modifiedViolations.add(ruleViolation);
    }
    else {
      addedViolations.add(ruleViolation);
    }
  }

  private String convertTagsToTag(String type)
  {
    if(type.equals(CommonConstants.TAGS)) {
      type = CommonConstants.TAG;
    }
    return type;
  }
  
  // validation for different rule on normalization
  private Map<String, Object> validateAndModelizeNormalization(List<Map<String, Object>> normalizations, String ruleType,
      Boolean isLanguageDependant, String code)
      throws Exception
  {
    List<String> includeEffect = new ArrayList<>();
    if (ruleType.equals(Constants.CLASSIFICATION)) {
      includeEffect.addAll(Arrays.asList(CommonConstants.TYPE, CommonConstants.TAXONOMY));
    }
    else if (ruleType.equals(Constants.STANDARDIZATION_AND_NORMALIZATION) && Boolean.TRUE.equals(isLanguageDependant)) {
      includeEffect.add(CommonConstants.ATTRIBUTE);
    }
    else if (ruleType.equals(Constants.STANDARDIZATION_AND_NORMALIZATION) && Boolean.FALSE.equals(isLanguageDependant)) {
      includeEffect.addAll(Arrays.asList(CommonConstants.ATTRIBUTE, CommonConstants.TAG, CommonConstants.TYPE, CommonConstants.TAXONOMY));
    }
    else {
      return new HashMap<>();
    }
    
    Map<String, Object> preparedNormalization = new HashMap<>();
    for (Map<String, Object> normalization : normalizations) {
      String entityId = (String) normalization.get(CommonConstants.ENTITY_ID_PROPERTY);
      String type = (String) normalization.get(ConfigTag.type.toString());
      try {
        normalization.remove(ConfigTag.entityAttributeType.toString());
        if (CommonConstants.TAGS.equals(type)) {
          type = validateAndPrepareModelForTag(normalization, type);
        }
        else if (CommonConstants.TAXONOMY.equals(type)) {
          List<String> taxonomies = (List<String>) normalization.get(INormalization.VALUES);
          List<String> validTaxonomies = getValidClassifiersForRule(taxonomies, VertexLabelConstants.ROOT_KLASS_TAXONOMY, code);
          normalization.put(INormalization.VALUES, validTaxonomies);
        }
        else if (CommonConstants.TYPE.equals(type)) {
          List<String> types = (List<String>) normalization.get(INormalization.VALUES);
          List<String> validTypes = getValidClassifiersForRule(types, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, code);
          normalization.put(INormalization.VALUES, validTypes);
        }
        else if (CommonConstants.ATTRIBUTE.equals(type)) {
          Vertex attribute = UtilClass.getVertexByCode(entityId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          boolean isTranslatable = attribute.getProperty(IAttribute.IS_TRANSLATABLE);
          String transformationType = (String) normalization.get(ConfigTag.transformationType.toString());
          TransformationType transformation = TransformationType.valueOf(transformationType);
          if (isLanguageDependant != isTranslatable) {
            throw new InvalidDataRuleTypeException();
          }
          String attributeType = attribute.getProperty(IAttribute.TYPE);
          if (CommonConstants.NUMERIC_TYPE.contains(attributeType)) {
            if (!TransformationType.NUMERIC_TRANSFORMATION_TYPE.contains(transformation)) {
              throw new InvalidDataRuleTypeException();
            }
          }
        }
        //need check for attribute types.

        if (includeEffect.contains(type)) {
          String id = entityId == null ? type : entityId;
          preparedNormalization.put(id, normalization);
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, code, entityId == null ? type : entityId);
      }
    }
    return preparedNormalization;
  }

  private String validateAndPrepareModelForTag(Map<String, Object> normalization, String type) throws Exception
  {
    type = type.toLowerCase();
    type = convertTagsToTag(type);
    normalization.put(ConfigTag.type.toString(), type);
    List<Map<String, Object>> importTagValues = (List<Map<String, Object>>) normalization.get(INormalization.TAG_VALUES);
    Map<String, Object> normalizationTagValues = new HashMap<>();
    for (Map<String, Object> tagValue : importTagValues) {
      String code = (String) tagValue.get(CommonConstants.ID_PROPERTY);
      tagValue.put(IDataRuleTagValues.INNER_TAG_ID, code);
      normalizationTagValues.put(code, tagValue);
    }

    String entityId = (String) normalization.get(INormalization.ENTITY_ID);
    Vertex vertex = UtilClass.getVertexByCode(entityId, VertexLabelConstants.ENTITY_TAG);

    List<String> tagValues = TagDbUtil.getTagValueIds(entityId);
    for (String tagValue : tagValues) {
      if (!normalizationTagValues.containsKey(tagValue)) {
        Map<String, Object> tagValuesToImport = new HashMap<>();
        tagValuesToImport.put(IDataRuleTagValues.INNER_TAG_ID, tagValue);
        tagValuesToImport.put(CommonConstants.ID_PROPERTY, tagValue);
        tagValuesToImport.put(CommonConstants.TO_PROPERTY, 0);
        tagValuesToImport.put(CommonConstants.FROM, 0);
        importTagValues.add(tagValuesToImport);
      }
    }
    return type;
  }

  private Map<String, Object> prepareDataRuleMap( Map<String, Object> importData)
  {
    Map<String, Object> initialDataRuleFields = new HashMap<>();
    String ruleType = (String) importData.get(CommonConstants.TYPE_PROPERTY);
    Boolean isLanguage = (Boolean) importData.get(IDataRule.IS_LANGUAGE_DEPENDENT);
    // by default null is selected
    if (isLanguage == null)
      isLanguage = false;
    if (ruleType.equals(Constants.CLASSIFICATION)) {
      initialDataRuleFields.put(IDataRule.IS_LANGUAGE_DEPENDENT, false);
      initialDataRuleFields.put(IDataRule.LANGUAGES, new ArrayList<>());
    }
    else if (Boolean.FALSE.equals(isLanguage)) {
      initialDataRuleFields.put(IDataRule.LANGUAGES, new ArrayList<>());
    }

    initialDataRuleFields.put(CommonConstants.CODE_PROPERTY, importData.get(CommonConstants.CODE_PROPERTY));
    initialDataRuleFields.put(CommonConstants.LABEL_PROPERTY, importData.get(CommonConstants.LABEL_PROPERTY));
    initialDataRuleFields.put(CommonConstants.IS_STANDARD, false);
    initialDataRuleFields.put(IDataRule.VERSION_ID, 0);
    initialDataRuleFields.put(IDataRule.ENDPOINTS, new ArrayList<>());
    return initialDataRuleFields;
  }

  public void addToFailureIds(List<Map<String, Object>> failedDataRuleList, Map<String, Object> dataRule)
  {
    Map<String, Object> failedDataRuleMap = new HashMap<>();
    failedDataRuleMap.put(ISummaryInformationModel.LABEL, dataRule.get(CommonConstants.LABEL_PROPERTY));
    failedDataRuleList.add(failedDataRuleMap);
  }
  
  private void handleOrganizations(Map<String,Object> importData, Vertex dataRule) throws Exception
  {
    List<String> importOrganizations = (List<String>) importData.get(ORGANIZATION);
    List<String> validOrganizations = ImportUtils.getValidNodeCodes(importOrganizations, VertexLabelConstants.ORGANIZATION, failure,
        dataRule.getProperty(CommonConstants.CODE_PROPERTY));

    Map<String, Vertex> existingOrganizations = DataRuleUtils.getOrganizationsForRule(dataRule);
    ArrayList<String> existingOrganizationIds = new ArrayList<>(existingOrganizations.keySet());
    List<String> addedOrganizationIds = ListUtils.subtract(validOrganizations, existingOrganizationIds);
    List<String> deletedOrganizationIds = ListUtils.subtract(existingOrganizationIds, validOrganizations);

    SaveDataRuleUtils.addOrganizations(dataRule, addedOrganizationIds);
    SaveDataRuleUtils.deleteOrganizations(dataRule, deletedOrganizationIds);
  }


  private List<Map<String, Object>> prepareResponseMap(List<Map<String, Object>> createdDataRuleList) throws Exception
  {
    List<Map<String, Object>> returnList = new ArrayList<>();
    for (Map<String, Object> dataRule : createdDataRuleList) {
      String dataRuleCode = (String) dataRule.get(CommonConstants.CODE_PROPERTY);
      Vertex dataRuleNode = UtilClass.getVertexByCode(dataRuleCode, VertexLabelConstants.DATA_RULE);
      Map<String, Object> returnMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode);

      Boolean isLanguageDependent = (Boolean) returnMap.get(IDataRule.IS_LANGUAGE_DEPENDENT);
      if (Boolean.TRUE.equals(isLanguageDependent)) {
        List<String> languages = (List<String>) returnMap.get(IDataRule.LANGUAGES);
        if (languages.isEmpty()) {
          Iterable<Vertex> verticesOfClass = UtilClass.getGraph().getVerticesOfClass(VertexLabelConstants.LANGUAGE);

          for (Vertex language : verticesOfClass) {
            languages.add(language.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
      GetDataRuleUtils.fillConfigDetails(returnMap);
      AuditLogUtils.fillAuditLoginfo(returnMap, dataRuleNode, Entities.RULES, Elements.UNDEFINED);

      Iterator<Vertex> vertexIterator = dataRuleNode.getVertices(Direction.IN, RelationshipLabelConstants.DATA_RULES).iterator();
      List<String> klassIds = new ArrayList<>();
      while (vertexIterator.hasNext()) {
        Vertex klassNode = vertexIterator.next();
        klassIds.add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      returnMap.put("klassIds", klassIds);
      List<String> physicalCatalogList = dataRuleNode.getProperty(IDataRule.PHYSICAL_CATALOG_IDS);
      returnMap.put("physicalCatalogList", physicalCatalogList);
      returnList.add(returnMap);
    }
    return returnList;
  }

  public Vertex validatePropertyRule(Map<String, Object> propertyToImport, String entityType, Vertex dataRule) throws Exception
  {
    boolean isLangDependent = dataRule.getProperty(IDataRule.IS_LANGUAGE_DEPENDENT);
    Vertex property = UtilClass.getVertexByCode((String) propertyToImport.get(CommonConstants.ENTITY_ID_PROPERTY), entityType);

    if (entityType.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)) {
      boolean isTranslatable = property.getProperty(IAttribute.IS_TRANSLATABLE);
      if (!isLangDependent && isTranslatable) {
        throw new InvalidDataRuleTypeException();
      }
    }
    return property;
  }

  protected void validateRule(Map<String, Object> propertyToImport, String entityType, Vertex property) throws Exception
  {
    if (entityType.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)) {
      String attributeLinkId = (String) propertyToImport.get(IDataRuleEntityRule.ATTRIBUTE_LINK_ID);
      String ruleListLinkId = (String) propertyToImport.get(IDataRuleEntityRule.RULE_LIST_LINK_ID);
      Boolean shouldCompareWithSysDate = (Boolean) propertyToImport.get(ConfigTag.shouldCompareWithSystemDate.toString());
      String attributeType = property.getProperty(IAttribute.TYPE);
      String propertyRuleType = (String) propertyToImport.get(ConfigTag.type.toString());
      RuleType ruleType = RuleType.valueOf(propertyRuleType);
      if (shouldCompareWithSysDate && !attributeType.equals(CommonConstants.DATE_ATTRIBUTE_TYPE)) {
        throw new InvalidDataRuleTypeException();
      }
      if (attributeLinkId != null) {
        UtilClass.getVertexByCode(attributeLinkId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        if (notAttributeLinkedType.contains(ruleType)) {
          throw new InvalidDataRuleTypeException();
        }
      }
      if (ruleListLinkId != null) {
        UtilClass.getVertexByCode(ruleListLinkId, VertexLabelConstants.RULE_LIST);
        if (!propertyRuleType.equals(RuleType.contains.name())) {
          throw new InvalidDataRuleTypeException();
        }
      }

      if (CommonConstants.NUMERIC_TYPE_WITHOUT_DATE.contains(attributeType)) {
        if (!ruleType.ruleContestants.contains(numeric)) {
          throw new InvalidDataRuleTypeException();
        }
      }
      else if (CommonConstants.DATE_ATTRIBUTE_TYPE.equals(attributeType)) {
        if (!ruleType.ruleContestants.contains(date)) {
          throw new InvalidDataRuleTypeException();
        }
      }
      else {
        if (!ruleType.ruleContestants.contains(literal)) {
          throw new InvalidDataRuleTypeException();
        }
      }
    }
    else if (entityType.equals(VertexLabelConstants.ENTITY_TAG)) {
      String typeOfRule = (String) propertyToImport.get(ConfigTag.type.toString());
      RuleType ruleType = RuleType.valueOf(typeOfRule);
      if (!ruleType.ruleContestants.contains(tag)) {
        throw new InvalidDataRuleTypeException();
      }
    }
  }

  public List<String> getValidClassifiersForRule(List<String> codes, String entityLabel, String entityCode)
  {
    if (codes == null)
      codes = new ArrayList<>();

    List<String> validCodes = new ArrayList<>();
    for (String code : codes) {
      try {
        Vertex vertexByCode = UtilClass.getVertexByCode(code, entityLabel);
        String taxonomyType = vertexByCode.getProperty(ITaxonomy.TAXONOMY_TYPE);
        if(CommonConstants.MINOR_TAXONOMY.equals(taxonomyType)){
          throw new InvalidTypeException();
        }
        validCodes.add(code);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, entityCode, code);
      }
    }
    return validCodes;
  }
  
  private String getVetexTypeByType(String type)
  {
    switch (type) {
      case CommonConstants.ATTRIBUTE:
        return VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
        
      case CommonConstants.TAG:
        return VertexLabelConstants.ENTITY_TAG;
        
       default :
         return null;
    }
  }

}
