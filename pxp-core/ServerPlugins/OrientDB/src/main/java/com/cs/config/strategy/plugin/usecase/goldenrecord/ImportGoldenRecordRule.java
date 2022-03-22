package com.cs.config.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.export.ExportGoldenRecordRuleList;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.config.interactor.exception.user.MandatoryFieldException;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
@SuppressWarnings("unchecked")
public class ImportGoldenRecordRule extends AbstractOrientPlugin {
  
  private static final String       SUCCESS_DATA_RULE_LIST = "successDataRuleList";
  private static final String       LATEST_SUPPLIER        = "latest";
  private static final String       SUPPLIER_PRIORITY      = "supplierPriority";
  private static final String       SUPPLIERS_TYPE         = "suppliers";
  private static final List<String> SUPPLIERS_TYPES        = Arrays.asList(SUPPLIER_PRIORITY, SUPPLIERS_TYPE);
  
  protected List<String> fieldsToExclude = Arrays.asList(IGoldenRecordRule.ATTRIBUTES, IGoldenRecordRule.TAGS, IGoldenRecordRule.KLASS_IDS,
      IGoldenRecordRule.TAXONOMY_IDS, IGoldenRecordRule.ORGANIZATIONS, IGoldenRecordRule.ENDPOINTS, IGoldenRecordRule.MERGE_EFFECT);
  
  public ImportGoldenRecordRule(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportGoldenRecordRule/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> goldenRuleList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> successGoldenRuleList = new ArrayList<>();
    List<Map<String, Object>> failedTGoldenRuleList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> responseMap = new ArrayList<Map<String,Object>>();
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.GOLDEN_RECORD_RULE, CommonConstants.CODE_PROPERTY);
    OrientVertexType mergeVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.MERGE_EFFECT, CommonConstants.CODE_PROPERTY);
    
    for (Map<String, Object> goldenRule : goldenRuleList) {
      try {
        Map<String, Object> goldenRuleMap = upsertGoldenRule(goldenRule, vertexType, mergeVertexType, failure);
        ImportUtils.addSuccessImportedInfo(successGoldenRuleList, goldenRule);
        responseMap.add(goldenRuleMap);
      }
      catch (Exception e) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedTGoldenRuleList, goldenRule, e);
      }
    }
    
    Map<String, Object> result = ImportUtils.prepareImportResponseMap(failure, successGoldenRuleList, failedTGoldenRuleList);
    result.put(SUCCESS_DATA_RULE_LIST, responseMap);
    return result;
  }
  
  private Map<String, Object> upsertGoldenRule(Map<String, Object> goldenRule, OrientVertexType vertexType,
      OrientVertexType mergeVertexType, IExceptionModel failure) throws Exception
  {
    String code = (String) goldenRule.get(CommonConstants.CODE_PROPERTY);
    validatePhysicalCatalogIDs(goldenRule);
    validationForNatureClass(goldenRule, failure, code);
    Vertex ruleNode;
    try {
      ruleNode = UtilClass.getVertexByCode(code, VertexLabelConstants.GOLDEN_RECORD_RULE);
      UtilClass.saveNode(goldenRule, ruleNode, fieldsToExclude);
    }catch (NotFoundException e) {
      ruleNode = UtilClass.createNode(goldenRule, vertexType, fieldsToExclude);
    }
    saveGoldenRecordRule(goldenRule, mergeVertexType, failure, code, ruleNode);
    UtilClass.getGraph().commit();
    Map<String, Object> returnMap = GoldenRecordRuleUtil.getGoldenRecordRuleFromNode(ruleNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, ruleNode, Entities.GOLDEN_RECORD_RULE, Elements.UNDEFINED);
    return returnMap;
  }

  /**
   * Only valid physical catalog code is added
   * @param goldenRule
   */
  private void validatePhysicalCatalogIDs(Map<String, Object> goldenRule)
  {
    List<String> catalogIds = (List<String>) goldenRule.get(IGoldenRecordRule.PHYSICAL_CATALOG_IDS);
    if(catalogIds == null)
      catalogIds = new ArrayList<>();
    catalogIds.retainAll(Constants.PHYSICAL_CATALOG_IDS);
  }

  /**
   * Validate nature class
   * @param goldenRule
   * @param code 
   * @param failure 
   * @throws MandatoryFieldException
   * @throws Exception
   */
  private void validationForNatureClass(Map<String, Object> goldenRule, IExceptionModel failure, String code) throws MandatoryFieldException, Exception
  {
    List<String> klassIds = (List<String>) goldenRule.get(IGoldenRecordRule.KLASS_IDS);
    if(klassIds.isEmpty())
      throw new MandatoryFieldException();
    else
      checkOnlyOneNatureClassIsPresent(klassIds, failure, code);
  }

  /**
   * Save Golden record rule property
   * @param goldenRule
   * @param mergeVertexType
   * @param failure
   * @param code
   * @param ruleNode
   * @throws Exception
   */
  private void saveGoldenRecordRule(Map<String, Object> goldenRule, OrientVertexType mergeVertexType, IExceptionModel failure, String code,
      Vertex ruleNode) throws Exception
  {
    manageAttributes(goldenRule, ruleNode, failure, code);
    manageTags(goldenRule, ruleNode, failure, code);
    manageKlassIds(goldenRule, ruleNode, failure, code);
    manageTaxonomy(goldenRule, ruleNode, failure, code);
    manageOrganizations(goldenRule, ruleNode, failure, code);
    manageModifiedMergeEffect(goldenRule, mergeVertexType, ruleNode, failure, code);
  }
  
  /**
   * validate and find diff for created, added and deleted merge effect
   * @param goldenRule
   * @param mergeVertexType
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageModifiedMergeEffect(Map<String, Object> goldenRule, OrientVertexType mergeVertexType, Vertex ruleNode,
      IExceptionModel failure, String code) throws Exception
  {
    Map<String, Object> mergeEffectMap = new HashMap<>();
    ExportGoldenRecordRuleList.fillMergeEffectData(ruleNode, mergeEffectMap);
    Map<String, Object> oldMergeEffect = (Map<String, Object>) mergeEffectMap.get(IGoldenRecordRule.MERGE_EFFECT);
    if(oldMergeEffect == null) {
      oldMergeEffect = new HashMap<>();
    }
    Iterator<Vertex> mergeEffectIterator = ruleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK)
        .iterator();
    Vertex mergeEffectNode;
    if (!mergeEffectIterator.hasNext()) {
      mergeEffectNode = UtilClass.createNode(new HashMap<>(), mergeVertexType, new ArrayList<>());
      ruleNode.addEdge(RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK, mergeEffectNode);
    }else {
      mergeEffectNode = mergeEffectIterator.next();
    }
    
    Map<String, Object> newMergeEffect = (Map<String, Object>) goldenRule.get(IGoldenRecordRule.MERGE_EFFECT);
    if(newMergeEffect == null) {
      newMergeEffect = new HashMap<>();
    }
    
    saveEffectEntities(oldMergeEffect, mergeEffectNode, IMergeEffect.ATTRIBUTES, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, failure, code, newMergeEffect);
    
    saveEffectEntities(oldMergeEffect, mergeEffectNode, IMergeEffect.TAGS, VertexLabelConstants.ENTITY_TAG, failure, code, newMergeEffect);
    
    saveEffectEntities(oldMergeEffect, mergeEffectNode, IMergeEffect.RELATIONSHIPS, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP, failure, code, newMergeEffect);
    
    saveEffectEntities(oldMergeEffect, mergeEffectNode, IMergeEffect.NATURE_RELATIONSHIPS, VertexLabelConstants.NATURE_RELATIONSHIP, failure, code, newMergeEffect);
  }

  /**
   * Update the entity by type
   * @param oldMergeEffect
   * @param mergeEffectNode
   * @param effectEntityType
   * @param entityType
   * @param failure
   * @param code
   * @param newMergeEffect
   * @throws Exception
   */
  private void saveEffectEntities(Map<String, Object> oldMergeEffect, Vertex mergeEffectNode, String effectEntityType, String entityType,
      IExceptionModel failure, String code, Map<String, Object> newMergeEffect) throws Exception
  {
    List<Map<String, Object>> oldEffectEntities = (List<Map<String, Object>>) oldMergeEffect.get(effectEntityType);
    List<String> oldEffectEntityCodes = new ArrayList<>();
    if (oldEffectEntities != null && !oldEffectEntities.isEmpty()) {
      oldEffectEntityCodes = oldEffectEntities.stream().map(a -> (String) a.get(IMergeEffectType.ENTITY_ID))
          .collect(Collectors.toList());
    }
    List<Map<String, Object>> newEffectEntities = (List<Map<String, Object>>) newMergeEffect.get(effectEntityType);
    List<Map<String, Object>> validEffectEntities = getValidMergeEntities(newEffectEntities, entityType, failure, code);
    List<Map<String, Object>> addedEffectEntities = new ArrayList<>();
    List<Map<String, Object>> modifiedEffectEntities = new ArrayList<>();
    List<String> newEntityCodes = new ArrayList<>();
    for (Map<String, Object> attribute : validEffectEntities) {
      String entityID = (String) attribute.get(IMergeEffectType.ENTITY_ID);
      if (oldEffectEntityCodes.contains(entityID)) {
        modifiedEffectEntities.add(attribute);
      }
      else {
        addedEffectEntities.add(attribute);
      }
      newEntityCodes.add(entityID);
    }
    List<String> deletedEntity = new ArrayList<>(oldEffectEntityCodes);
    deletedEntity.removeAll(newEntityCodes);
    
    handleAddedEffectEntities(mergeEffectNode, addedEffectEntities, entityType);
    GoldenRecordRuleUtil.manageModifiedEffectEntity(mergeEffectNode, modifiedEffectEntities);
    GoldenRecordRuleUtil.handleDeletedEffectEntities(mergeEffectNode, deletedEntity, new ArrayList<String>(), null);
  }

  /**
   * validate and find diff for created, added and deleted Organization
   * @param goldenRule
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageOrganizations(Map<String, Object> goldenRule, Vertex ruleNode, IExceptionModel failure, String code) throws Exception
  {
    List<String> oldOrganization = getLinkedVertexCodes(ruleNode, RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK);
    List<String> organization = (List<String>) goldenRule.get(IGoldenRecordRule.ORGANIZATIONS);
    List<String> validOrganization = ImportUtils.getValidNodeCodes(organization, VertexLabelConstants.ORGANIZATION, failure, code);
    List<String> addedEntity = new ArrayList<>(validOrganization);
    addedEntity.removeAll(oldOrganization);
    List<String> deletedEntity = new ArrayList<>(oldOrganization);
    deletedEntity.removeAll(validOrganization);
    GoldenRecordRuleUtil.handleDeletedOrganizations(ruleNode, deletedEntity, addedEntity);
    GoldenRecordRuleUtil.handleAddedOrganizations(ruleNode, addedEntity);
  }

  /**
   * validate and find diff for created, added and deleted Taxonomies
   * @param goldenRule
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageTaxonomy(Map<String, Object> goldenRule, Vertex ruleNode, IExceptionModel failure, String code) throws Exception
  {
    List<String> oldTaxonomies = getLinkedVertexCodes(ruleNode, RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK);
    List<String> taxonomies = (List<String>) goldenRule.get(IGoldenRecordRule.TAXONOMY_IDS);
    List<String> validTaxonomies = ImportUtils.getValidNodeCodes(taxonomies, VertexLabelConstants.ROOT_KLASS_TAXONOMY, failure, code);
    List<String> addedEntity = new ArrayList<>(validTaxonomies);
    addedEntity.removeAll(oldTaxonomies);
    List<String> deletedEntity = new ArrayList<>(oldTaxonomies);
    deletedEntity.removeAll(validTaxonomies);
    GoldenRecordRuleUtil.handleDeletedTaxonomies(ruleNode, deletedEntity, addedEntity);
    GoldenRecordRuleUtil.handleAddedTaxonomies(ruleNode, addedEntity);
  }

  /**
   * validate and find diff for created, added and deleted merge effect
   * @param goldenRule
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageKlassIds(Map<String, Object> goldenRule, Vertex ruleNode, IExceptionModel failure, String code) throws Exception
  {
    List<String> oldKlasses = getLinkedVertexCodes(ruleNode, RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK);
    List<String> klasses = (List<String>) goldenRule.get(IGoldenRecordRule.KLASS_IDS);
    List<String> validklasses = ImportUtils.getValidNodeCodes(klasses, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, failure, code);
    List<String> addedEntity = new ArrayList<>(validklasses);
    addedEntity.removeAll(oldKlasses);
    List<String> deletedEntity = new ArrayList<>(oldKlasses);
    deletedEntity.removeAll(validklasses);
    GoldenRecordRuleUtil.handleDeletedKlasses(ruleNode, deletedEntity, addedEntity);
    GoldenRecordRuleUtil.handleAddedKlasses(ruleNode, addedEntity);
  }

  /**
   * Update tags
   * validate and find diff for created, added and deleted tags
   * @param goldenRule
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageTags(Map<String, Object> goldenRule, Vertex ruleNode, IExceptionModel failure, String code) throws Exception
  {
    List<String> oldTags = getLinkedVertexCodes(ruleNode, RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAG_LINK);
    List<String> tags = (List<String>) goldenRule.get(IGoldenRecordRule.TAGS);
    List<String> validTags = ImportUtils.getValidNodeCodes(tags, VertexLabelConstants.ENTITY_TAG, failure, code);
    List<String> addedEntity = new ArrayList<>(validTags);
    addedEntity.removeAll(oldTags);
    List<String> deletedEntity = new ArrayList<>(oldTags);
    deletedEntity.removeAll(validTags);
    GoldenRecordRuleUtil.handleDeletedTags(ruleNode, deletedEntity, addedEntity);
    GoldenRecordRuleUtil.handleAddedTags(ruleNode, addedEntity);
  }

  /**
   * Update Attributes
   * validate and find diff for created, added and deleted attributes
   * @param goldenRule
   * @param ruleNode
   * @param failure
   * @param code
   * @throws Exception
   */
  private void manageAttributes(Map<String, Object> goldenRule, Vertex ruleNode, IExceptionModel failure, String code) throws Exception
  {
    List<String> oldAttributes = getLinkedVertexCodes(ruleNode, RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK);
    List<String> attributes = (List<String>) goldenRule.get(IGoldenRecordRule.ATTRIBUTES);
    List<String> validAttributes = ImportUtils.getValidNodeCodes(attributes, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, failure, code);
    List<String> addedEntity = new ArrayList<>(validAttributes);
    addedEntity.removeAll(oldAttributes);
    List<String> deletedEntity = new ArrayList<>(oldAttributes);
    deletedEntity.removeAll(validAttributes);
    GoldenRecordRuleUtil.handleDeletedAttributes(ruleNode, deletedEntity, addedEntity);
    GoldenRecordRuleUtil.handleAddedAttributes(ruleNode, addedEntity);
  }

  /**
   * Get linked entity code by type for rule
   * @param ruleNode
   * @param labelType
   * @return
   */
  private List<String> getLinkedVertexCodes(Vertex ruleNode, String labelType)
  {
    Iterable<Vertex> vertices = ruleNode.getVertices(Direction.OUT, labelType);
    List<String> oldEntityCodes = StreamSupport.stream(vertices.spliterator(), false)
        .map(v -> (String) v.getProperty(CommonConstants.CODE_PROPERTY)).collect(Collectors.toList());
    return oldEntityCodes;
  }

  /**
   * Checked only one valid nature class is present
   * @param klassIds
   * @param failure
   * @param entityCode
   * @throws Exception
   */
  private void checkOnlyOneNatureClassIsPresent(List<String> klassIds, IExceptionModel failure, String entityCode) throws Exception
  {
    int flag = 0;
    for (String code : klassIds) {
      try {
        Vertex vertex = UtilClass.getVertexByCode(code, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        Boolean isnature = vertex.getProperty(IKlassModel.IS_NATURE);
        if (isnature) {
          flag++;
          if (flag > 1)
            throw new Exception("More_Than_One_Nature_Class_Found ");
        }
      }
      catch (NotFoundException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, entityCode, code);
      }
    }
    
    if (flag == 0) {
      throw new Exception("No_Nature_Class_Found");
    }
  }

  public void handleAddedEffectEntities(Vertex mergeEffect,
      List<Map<String, Object>> entities, String vertexLabel) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.EFFECT_TYPE,
        CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> entityMap : entities) {
      String entityId = (String) entityMap.get(IMergeEffectType.ENTITY_ID);
      Vertex entityVertex = UtilClass.getVertexByCode(entityId, vertexLabel);
      Vertex effectType = UtilClass.createNode(entityMap, vertexType,
          Arrays.asList(IMergeEffectType.SUPPLIER_IDS));
      mergeEffect.addEdge(RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK, effectType);
      effectType.addEdge(RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK, entityVertex);
      List<String> supplierIds = (List<String>) entityMap.get(IMergeEffectType.SUPPLIER_IDS);
      if(supplierIds != null) {
        GoldenRecordRuleUtil.linkSuppliersToEffect(effectType,
            supplierIds);
      }
    }
  }
 
  /**
   * Validate the Merge entity by type and supplier ID
   * @param addedEffects
   * @param entityLabel
   * @param failure
   * @param code
   * @return
   */
  private List<Map<String, Object>> getValidMergeEntities(List<Map<String, Object>> addedEffects, String entityLabel, IExceptionModel failure, String code){
    if(addedEffects == null) {
      addedEffects = new ArrayList<>();
    }
    List<Map<String, Object>> validMergeEffects = new ArrayList<>();
    for(Map<String, Object> mergeEffect : addedEffects) {
      String entityId = (String) mergeEffect.get(ConfigTag.entityId.toString());
      try {
        UtilClass.getVertexByCode(entityId, entityLabel);
        addValidSupplierId(failure, mergeEffect, entityId);
        String type = (String) mergeEffect.get(IMergeEffectType.TYPE);
        if (StringUtils.isEmpty(type) || !SUPPLIERS_TYPES.contains(type)) {
          mergeEffect.put(IMergeEffectType.TYPE, LATEST_SUPPLIER);
        }
        validMergeEffects.add(mergeEffect);
      }catch(Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, code, entityId);
      }
    }
    
    return validMergeEffects;
  }

  /**
   * Validate the Supplier ID
   * @param failure
   * @param mergeEffect
   * @param entityId
   */
  private void addValidSupplierId(IExceptionModel failure, Map<String, Object> mergeEffect, String entityId)
  {
    List<String> supplierId = (List<String>) mergeEffect.get(IMergeEffectType.SUPPLIER_IDS);
    if (supplierId == null)
      supplierId = new ArrayList<>();
    List<String> validCodes = new ArrayList<>();
    for (String id : supplierId) {
      try {
        UtilClass.getVertexByCode(id, VertexLabelConstants.ORGANIZATION);
        validCodes.add(id);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, entityId, id);
      }
    }
    mergeEffect.put(IMergeEffectType.SUPPLIER_IDS, validCodes);
  }
}
