package com.cs.config.strategy.plugin.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableBoolean;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.exception.goldenrecord.GoldenRecordRuleNotFoundException;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IModifiedMergeEffectModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class SaveGoldenRecordRule extends AbstractOrientPlugin {
  
  public SaveGoldenRecordRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveGoldenRecordRule/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String ruleId = (String) requestMap.get(ISaveGoldenRecordRuleModel.ID);
    Vertex goldenRecordRule = null;
    try {
      goldenRecordRule = UtilClass.getVertexById(ruleId, VertexLabelConstants.GOLDEN_RECORD_RULE);
    }
    catch (NotFoundException e) {
      throw new GoldenRecordRuleNotFoundException();
    }
    
    MutableBoolean needToEvaluateGoldenRecordRuleBucket = new MutableBoolean();
    List<String> physicalCatalogIdsForEvaluation = new ArrayList<>();
    saveGoldenRecordRule(goldenRecordRule, requestMap, physicalCatalogIdsForEvaluation, needToEvaluateGoldenRecordRuleBucket);
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> returnMap = GoldenRecordRuleUtil
        .getGoldenRecordRuleFromNode(goldenRecordRule);
    returnMap.put(IGetGoldenRecordRuleModel.NEED_TO_EVALUATE_GOLDEN_RECORD_RULE_BUCKET, needToEvaluateGoldenRecordRuleBucket.getValue());
    returnMap.put(IGetGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS, physicalCatalogIdsForEvaluation);
    AuditLogUtils.fillAuditLoginfo(returnMap, goldenRecordRule, Entities.GOLDEN_RECORD_RULE, Elements.UNDEFINED);
    return returnMap;
  }
  
  public void saveGoldenRecordRule(Vertex goldenRecordRuleNode, Map<String, Object> requestMap,
      List<String> physicalCatalogIdsForEvaluation, MutableBoolean needToEvaluateGoldenRecordRuleBucket)
      throws Exception
  {
    Boolean isMatchConditionsModified = checkIfMatchConditionsAreModified(requestMap);
    Boolean isPhysicalCatalogsChanged = checkIfPhysicalCatalogsAreChanged(goldenRecordRuleNode, requestMap);
    if (isPhysicalCatalogsChanged || isMatchConditionsModified) {
      preparePhysicalCatalogIdsForEvaluation(goldenRecordRuleNode, requestMap, isMatchConditionsModified, physicalCatalogIdsForEvaluation);
      needToEvaluateGoldenRecordRuleBucket.setValue(true);
    }
    
    String label = (String) requestMap.get(ISaveGoldenRecordRuleModel.LABEL);
    goldenRecordRuleNode
        .setProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY), label);
    goldenRecordRuleNode.setProperty(IGoldenRecordRule.ICON,
        (String) requestMap.get(ISaveGoldenRecordRuleModel.ICON));
    goldenRecordRuleNode.setProperty(IGoldenRecordRule.PHYSICAL_CATALOG_IDS,
        (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS));
    goldenRecordRuleNode.setProperty(IGoldenRecordRule.IS_AUTO_CREATE,
        requestMap.get(IGoldenRecordRule.IS_AUTO_CREATE));
    manageAddedDeletedAttributes(goldenRecordRuleNode, requestMap);
    manageAddedDeletedTags(goldenRecordRuleNode, requestMap);
    manageAddedDeletedKlasses(goldenRecordRuleNode, requestMap);
    manageAddedDeletedTaxonomies(goldenRecordRuleNode, requestMap);
    manageAddedDeletedOrganizations(goldenRecordRuleNode, requestMap);
    manageAddedDeletedEndpoints(goldenRecordRuleNode, requestMap);
    manageModifiedMergeEffect(goldenRecordRuleNode, requestMap);
  }
  
  public void manageAddedDeletedAttributes(Vertex goldenRecordRuleNode,
      Map<String, Object> requestMap) throws Exception
  {
    List<String> addedAttributes = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.ADDED_ATTRIBUTES);
    List<String> deletedAttributes = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_ATTRIBUTES);
    GoldenRecordRuleUtil.handleDeletedAttributes(goldenRecordRuleNode, deletedAttributes, addedAttributes);
    GoldenRecordRuleUtil.handleAddedAttributes(goldenRecordRuleNode, addedAttributes);
  }
  
  public void manageAddedDeletedTags(Vertex goldenRecordRuleNode, Map<String, Object> requestMap)
      throws Exception
  {
    List<String> addedTags = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_TAGS);
    List<String> deletedTags = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_TAGS);
    GoldenRecordRuleUtil.handleDeletedTags(goldenRecordRuleNode, deletedTags, addedTags);
    GoldenRecordRuleUtil.handleAddedTags(goldenRecordRuleNode, addedTags);
  }
  
  public void manageAddedDeletedKlasses(Vertex goldenRecordRuleNode, Map<String, Object> requestMap)
      throws Exception
  {
    List<String> addedKlasses = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.ADDED_KLASSES);
    List<String> deletedKlasses = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_KLASSES);
    GoldenRecordRuleUtil.handleDeletedKlasses(goldenRecordRuleNode, deletedKlasses, addedKlasses);
    GoldenRecordRuleUtil.handleAddedKlasses(goldenRecordRuleNode, addedKlasses);
  }
  
  public void manageAddedDeletedTaxonomies(Vertex goldenRecordRuleNode,
      Map<String, Object> requestMap) throws Exception
  {
    List<String> addedTaxonomies = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.ADDED_TAXONOMIES);
    List<String> deletedTaxonomies = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_TAXONOMIES);
    GoldenRecordRuleUtil.handleDeletedTaxonomies(goldenRecordRuleNode, deletedTaxonomies, addedTaxonomies);
    GoldenRecordRuleUtil.handleAddedTaxonomies(goldenRecordRuleNode, addedTaxonomies);
  }
  
  public void manageAddedDeletedOrganizations(Vertex goldenRecordRuleNode,
      Map<String, Object> requestMap) throws Exception
  {
    List<String> addedOrganizations = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.ADDED_ORGANIZATIONS);
    List<String> deletedOrganizations = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_ORGANIZATIONS);
    GoldenRecordRuleUtil.handleDeletedOrganizations(goldenRecordRuleNode, deletedOrganizations, addedOrganizations);
    GoldenRecordRuleUtil.handleAddedOrganizations(goldenRecordRuleNode, addedOrganizations);
  }
  
  public void manageAddedDeletedEndpoints(Vertex goldenRecordRuleNode,
      Map<String, Object> requestMap) throws Exception
  {
    List<String> addedEndpoints = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.ADDED_ENDPOINTS);
    List<String> deletedEndpoints = (List<String>) requestMap
        .get(ISaveGoldenRecordRuleModel.DELETED_ENDPOINTS);
    GoldenRecordRuleUtil.handleDeletedEndpoints(goldenRecordRuleNode, deletedEndpoints, addedEndpoints);
    GoldenRecordRuleUtil.handleAddedEndpoints(goldenRecordRuleNode, addedEndpoints);
  }
  
  public void manageModifiedMergeEffect(Vertex goldenRecordRuleNode, Map<String, Object> requestMap)
      throws Exception
  {
    Map<String, Object> modifiedMergeEffect = (Map<String, Object>) requestMap
        .get(ISaveGoldenRecordRuleModel.MODIFIED_MERGE_EFFECT);
    if (modifiedMergeEffect == null || modifiedMergeEffect.isEmpty()) {
      return;
    }
    Iterator<Vertex> mergeEffectIterator = goldenRecordRuleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK)
        .iterator();
    if (!mergeEffectIterator.hasNext()) {
      return;
    }
    Vertex mergeEffect = mergeEffectIterator.next();
    
    // Merge Attributes
    List<String> existingEntityIds = new ArrayList<String>();
    List<String> deletedEffectAttributes = (List<String>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.DELETED_EFFECT_ATTRIBUTES);
    GoldenRecordRuleUtil.handleDeletedEffectEntities(mergeEffect, deletedEffectAttributes,
        existingEntityIds, CommonConstants.ATTRIBUTES);
    List<Map<String, Object>> addedEffectAttributes = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.ADDED_EFFECT_ATTRIBUTES);
    GoldenRecordRuleUtil.handleAddedEffectEntities(mergeEffect, addedEffectAttributes,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, existingEntityIds);
    List<Map<String, Object>> modifiedEffectAttributes = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.MODIFIED_EFFECT_ATTRIBUTES);
    GoldenRecordRuleUtil.manageModifiedEffectEntity(mergeEffect, modifiedEffectAttributes);
    
    // Merge Tags
    existingEntityIds.clear();
    List<String> deletedEffectTags = (List<String>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.DELETED_EFFECT_TAGS);
    GoldenRecordRuleUtil.handleDeletedEffectEntities(mergeEffect, deletedEffectTags,
        existingEntityIds, CommonConstants.TAGS);
    List<Map<String, Object>> addedEffectTags = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.ADDED_EFFECT_TAGS);
    GoldenRecordRuleUtil.handleAddedEffectEntities(mergeEffect, addedEffectTags,
        VertexLabelConstants.ENTITY_TAG, existingEntityIds);
    List<Map<String, Object>> modifiedEffectTags = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.MODIFIED_EFFECT_TAGS);
    GoldenRecordRuleUtil.manageModifiedEffectEntity(mergeEffect, modifiedEffectTags);
    
    // Merge Relationships
    existingEntityIds.clear();
    List<String> deletedEffectRelationships = (List<String>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.DELETED_EFFECT_RELATIONSHIPS);
    GoldenRecordRuleUtil.handleDeletedEffectEntities(mergeEffect, deletedEffectRelationships,
        existingEntityIds, CommonConstants.RELATIONSHIPS);
    List<Map<String, Object>> addedEffectRelationships = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.ADDED_EFFECT_RELATIONSHIPS);
    GoldenRecordRuleUtil.handleAddedEffectEntities(mergeEffect, addedEffectRelationships,
        VertexLabelConstants.ROOT_RELATIONSHIP, existingEntityIds);
    List<Map<String, Object>> modifiedEffectRelationships = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.MODIFIED_EFFECT_RELATIONSHIPS);
    GoldenRecordRuleUtil.manageModifiedEffectEntity(mergeEffect, modifiedEffectRelationships);
    
    // Merge Nature Relationships
    existingEntityIds.clear();
    List<String> deletedEffectNatureRelationships = (List<String>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.DELETED_EFFECT_NATURE_RELATIONSHIPS);
    GoldenRecordRuleUtil.handleDeletedEffectEntities(mergeEffect, deletedEffectNatureRelationships,
        existingEntityIds, CommonConstants.NATURE_RELATIONSHIPS);
    List<Map<String, Object>> addedEffectNatureRelationships = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.ADDED_EFFECT_NATURE_RELATIONSHIPS);
    GoldenRecordRuleUtil.handleAddedEffectEntities(mergeEffect, addedEffectNatureRelationships,
        VertexLabelConstants.ROOT_RELATIONSHIP, existingEntityIds);
    List<Map<String, Object>> modifiedEffectNatureRelationships = (List<Map<String, Object>>) modifiedMergeEffect
        .get(IModifiedMergeEffectModel.MODIFIED_EFFECT_NATURE_RELATIONSHIPS);
    GoldenRecordRuleUtil.manageModifiedEffectEntity(mergeEffect, modifiedEffectNatureRelationships);
  }
  
  private Boolean checkIfMatchConditionsAreModified(Map<String, Object> requestMap)
  {
    return ifMatchConditionsAreAdded(requestMap) || ifMatchConditionsAreDeleted(requestMap);
  }
  
  private Boolean ifMatchConditionsAreDeleted(Map<String, Object> requestMap)
  {
    List<String> deletedTags = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.DELETED_TAGS);
    List<String> deletedAttributes = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.DELETED_ATTRIBUTES);
    List<String> deletedOrganizations = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.DELETED_ORGANIZATIONS);
    List<String> deletedKlasses = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.DELETED_KLASSES);
    List<String> deletedTaxonomies = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.DELETED_TAXONOMIES);
    
    if (!deletedTags.isEmpty() || !deletedAttributes.isEmpty() || !deletedOrganizations.isEmpty() || !deletedKlasses.isEmpty()
        || !deletedTaxonomies.isEmpty()) {
      return true;
    }
    return false;
  }
  
  private Boolean ifMatchConditionsAreAdded(Map<String, Object> requestMap)
  {
    List<String> addedTags = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_TAGS);
    List<String> addedAttributes = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_ATTRIBUTES);
    List<String> addedOrganizations = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_ORGANIZATIONS);
    List<String> addedKlasses = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_KLASSES);
    List<String> addedTaxonomies = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.ADDED_TAXONOMIES);
    
    if (!addedTags.isEmpty() || !addedAttributes.isEmpty() || !addedOrganizations.isEmpty() || !addedKlasses.isEmpty()
        || !addedTaxonomies.isEmpty()) {
      return true;
    }
    return false;
  }
  
  private void preparePhysicalCatalogIdsForEvaluation(Vertex goldenRecordRuleNode, Map<String, Object> requestMap,
      Boolean isMatchConditionsModified, List<String> physicalCatalogIdsForEvaluation)
  {
    List<String> existingPhysicalCatalogs = goldenRecordRuleNode.getProperty(IGoldenRecordRule.PHYSICAL_CATALOG_IDS);
    List<String> newlyModifiedPhysicalCatalogs = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS);
    
    if (!isMatchConditionsModified) {
      for (String newPhysicalCatalog : newlyModifiedPhysicalCatalogs) {
        if (existingPhysicalCatalogs.contains(newPhysicalCatalog)) {
          existingPhysicalCatalogs.remove(newPhysicalCatalog);
        }
        else {
          physicalCatalogIdsForEvaluation.add(newPhysicalCatalog);
        }
      }
      physicalCatalogIdsForEvaluation.addAll(existingPhysicalCatalogs);
    }
    else {
      for (String newPhysicalCalCatalog : newlyModifiedPhysicalCatalogs) {
        if (existingPhysicalCatalogs.contains(newPhysicalCalCatalog)) {
          existingPhysicalCatalogs.remove(newPhysicalCalCatalog);
        }
        physicalCatalogIdsForEvaluation.add(newPhysicalCalCatalog);
      }
      physicalCatalogIdsForEvaluation.addAll(existingPhysicalCatalogs);
    }
  }
  
  private Boolean checkIfPhysicalCatalogsAreChanged(Vertex goldenRecordRuleNode, Map<String, Object> requestMap)
  {
    List<String> availablePhysicalCatalogs = getAvailablePhysicalCatalogsForGoldenRecordRule(requestMap);
    List<String> existingPhysicalCatalogs = goldenRecordRuleNode.getProperty(IGoldenRecordRule.PHYSICAL_CATALOG_IDS);
    List<String> newlyModifiedPhysicalCatalogs = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.PHYSICAL_CATALOG_IDS);
    
    if (newlyModifiedPhysicalCatalogs.size() == existingPhysicalCatalogs.size()
        && newlyModifiedPhysicalCatalogs.containsAll(existingPhysicalCatalogs)) {
      return false;
    }
    if ((existingPhysicalCatalogs.isEmpty() || existingPhysicalCatalogs.containsAll(availablePhysicalCatalogs))
        && (newlyModifiedPhysicalCatalogs.isEmpty() || newlyModifiedPhysicalCatalogs.containsAll(availablePhysicalCatalogs))) {
      return false;
    }
    return true;
  }
  
  private List<String> getAvailablePhysicalCatalogsForGoldenRecordRule(Map<String, Object> requestMap)
  {
    List<String> availablePhysicalCatalogs = (List<String>) requestMap.get(ISaveGoldenRecordRuleModel.AVAILABLE_PHYSICAl_CATALOGS);
    availablePhysicalCatalogs.remove("dataIntegration");
    return availablePhysicalCatalogs;
  }
  
}
