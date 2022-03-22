package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.klassinstance.GetConfigDetailsWithoutPermissions;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForSwitchType extends GetConfigDetailsWithoutPermissions {
  
  public GetConfigDetailsForSwitchType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForSwitchType/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> klassIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.KLASS_IDS);
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.TAXONOMY_IDS);
    
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel
        .setEndpointId((String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID));
    helperModel.setOrganizationId(
        (String) requestMap.get(IMulticlassificationRequestModel.ORAGANIZATION_ID));
    helperModel.setPhysicalCatalogId(
        (String) requestMap.get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID));
    
    List<String> klassIdsToAdd = new ArrayList<>();
    List<String> taxonomyIdsToAdd = new ArrayList<>();
    fillTypesToAddFromClassificationRules(requestMap, klassIdsToAdd, taxonomyIdsToAdd, helperModel);
    klassIds.addAll(klassIdsToAdd);
    selectedTaxonomyIds.addAll(taxonomyIdsToAdd);
    
    Map<String, Object> mapToReturn = getConfigDetailWithoutPermissions(requestMap);
    
    List<String> deletedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.DELETED_TAXONOMY_IDS);
    List<String> addedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.ADDED_TAXONOMY_IDS);
    taxonomyIds.removeAll(addedTaxonomyIds);
    List<String> allTaxonomyIdsToBeAdded = new ArrayList<>(taxonomyIdsToAdd);
    allTaxonomyIdsToBeAdded.addAll(addedTaxonomyIds);
    
    List<String> allTaxonomyIds = new ArrayList<>(selectedTaxonomyIds);
    allTaxonomyIds.addAll(deletedTaxonomyIds);
    
    consolidateTaxonomyHierarchyIds(allTaxonomyIds, mapToReturn);
    
    Map<String, Collection<String>> taxonomyHierarchies = (Map<String, Collection<String>>) mapToReturn
        .get(IConfigDetailsForSwitchTypeResponseModel.TAXONOMY_HIERARCHIES);
    List<String> addedAndDeletedTaxonomyIds = new ArrayList<String>();
    Collection<String> addedTaxonomyIdAndItsParentIds = new ArrayList<String>();
    
    for (String taxonomyId : allTaxonomyIdsToBeAdded) {
      if (taxonomyHierarchies.containsKey(taxonomyId)) {
        addedTaxonomyIdAndItsParentIds.addAll(taxonomyHierarchies.get(taxonomyId));
        Collection<String> currentTaxonomyIdAndItsParentId = taxonomyHierarchies.get(taxonomyId);
        List<String> newlyAddedTaxonomyIds = new ArrayList<String>(currentTaxonomyIdAndItsParentId);
        newlyAddedTaxonomyIds.removeAll(taxonomyIds);
        addedAndDeletedTaxonomyIds.addAll(newlyAddedTaxonomyIds);
      }
    }
    
    for (String taxonomyId : deletedTaxonomyIds) {
      if (taxonomyHierarchies.containsKey(taxonomyId)) {
        List<String> removedTaxonomyIds = new ArrayList<String>(
            taxonomyHierarchies.get(taxonomyId));
        // remove addedTaxonomy Ids if present in addedTaxonomyIds or its
        // parent(when remove last
        // taxonomy its parent is sent in addedTaxonomy)
        removedTaxonomyIds.removeAll(addedTaxonomyIdAndItsParentIds);
        addedAndDeletedTaxonomyIds.addAll(removedTaxonomyIds);
      }
    }
    mapToReturn.put(IConfigDetailsForSwitchTypeResponseModel.KLASS_IDS_TO_ADD, klassIdsToAdd);
    mapToReturn.put(IConfigDetailsForSwitchTypeResponseModel.TAXONOMY_IDS_TO_ADD, taxonomyIdsToAdd);
    
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    
    Set<String> taxonomyForMastertags = new HashSet<>();
    taxonomyForMastertags.addAll(addedAndDeletedTaxonomyIds);
    taxonomyForMastertags.addAll(taxonomyIds);
    
    for (String taxonomyId : taxonomyForMastertags) {
      Map<String, Object> taxonomyMap = (Map<String, Object>) referencedTaxonomyMap.get(taxonomyId);
      if (taxonomyMap == null) {
        taxonomyMap = new HashMap<>();
        referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      }
      fillParentTagDetail(taxonomyMap,
          UtilClass.getVertexById(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY));
    }
    return mapToReturn;
  }
  
  protected void fillTypesToAddFromClassificationRules(Map<String, Object> requestMap,
      List<String> klassIdsToAdd, List<String> taxonomyIdsToAdd,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    // allKlassIds and allTaxonoyIds contains existing klassIds and taxonomIds
    // and added/removed
    // klassIds or taxonomyIds
    List<String> allKlassIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.KLASS_IDS);
    List<String> allTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.TAXONOMY_IDS);
    
    // List<String> addedKlassIds = (List<String>)
    // requestMap.get(IConfigDetailsForSwitchTypeRequestModel.ADDED_KLASS_IDS);
    for (String klassId : allKlassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      String query = getDataRulesQuery(klassNode, helperModel.getOrganizationId(),
          helperModel.getPhysicalCatalogId(), helperModel.getEndpointId(),
          RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
      Iterable<Vertex> ruleVertices = executeQuery(query);
      // Iterable<Vertex> ruleVertices = klassNode.getVertices(Direction.OUT,
      // RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
      for (Vertex rule : ruleVertices) {
        String ruleType = rule.getProperty(IDataRule.TYPE);
        if (ruleType.equals(CommonConstants.CLASSIFICATION)) {
          if (!checkWhetherCauseIsSatisfied(rule, klassId, true, allKlassIds, allTaxonomyIds)) {
            continue;
          }
          fillKlassAndTaxonomiesToAdd(requestMap, rule, klassIdsToAdd, taxonomyIdsToAdd);
        }
      }
    }
    
    // List<String> addedTaxonomyIds = (List<String>)
    // requestMap.get(IConfigDetailsForSwitchTypeRequestModel.ADDED_TAXONOMY_IDS);
    for (String taxonomyId : allTaxonomyIds) {
      
      Vertex taxonomyNode = UtilClass.getVertexById(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      String query = getDataRulesQuery(taxonomyNode, helperModel.getOrganizationId(),
          helperModel.getPhysicalCatalogId(), helperModel.getEndpointId(),
          RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
      Iterable<Vertex> ruleVertices = executeQuery(query);
      // Iterable<Vertex> ruleVertices = taxonomyNode.getVertices(Direction.OUT,
      // RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
      for (Vertex rule : ruleVertices) {
        String ruleType = rule.getProperty(IDataRule.TYPE);
        if (ruleType.equals(CommonConstants.CLASSIFICATION)) {
          if (!checkWhetherCauseIsSatisfied(rule, taxonomyId, false, allKlassIds, allTaxonomyIds)) {
            continue;
          }
          fillKlassAndTaxonomiesToAdd(requestMap, rule, klassIdsToAdd, taxonomyIdsToAdd);
        }
      }
    }
  }
  
  private Vertex getNormalizationNodeFromIterator(Iterator<Vertex> normalizationIterator)
      throws Exception
  {
    
    Vertex normalizationNode = null;
    if (normalizationIterator.hasNext()) {
      normalizationNode = normalizationIterator.next();
    }
    if (normalizationIterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return normalizationNode;
  }
  
  protected Boolean checkWhetherCauseIsSatisfied(Vertex rule, String addedTypeId,
      Boolean isKlassAdded, List<String> klassIds, List<String> taxonomyIds)
  {
    List<String> klassCauseIds = getEntityCauseIdsFromRule(rule, "klass");
    List<String> taxonomyCauseIds = getEntityCauseIdsFromRule(rule, "taxonomy");
    
    //
    if (!klassCauseIds.contains(addedTypeId) && !taxonomyCauseIds.contains(addedTypeId)) {
      return false;
    }
    
    if (isKlassAdded && !taxonomyCauseIds.isEmpty()) {
      taxonomyCauseIds.retainAll(taxonomyIds);
      if (taxonomyCauseIds.isEmpty()) {
        return false;
      }
    }
    else if (!isKlassAdded && !klassCauseIds.isEmpty()) {
      klassCauseIds.retainAll(klassIds);
      if (klassCauseIds.isEmpty()) {
        return false;
      }
    }
    
    return true;
  }
  
  protected List<String> getEntityCauseIdsFromRule(Vertex rule, String type)
  {
    String entityCauseLink = null;
    
    if (type.equals("klass")) {
      entityCauseLink = RelationshipLabelConstants.HAS_KLASS_RULE_LINK;
    }
    else {
      entityCauseLink = RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK;
    }
    
    List<String> entityCauseIds = new ArrayList<>();
    Iterable<Vertex> entityCauseVertices = rule.getVertices(Direction.IN, entityCauseLink);
    for (Vertex causeVertex : entityCauseVertices) {
      entityCauseIds.add(UtilClass.getCodeNew(causeVertex));
    }
    
    return entityCauseIds;
  }
  
  protected void fillKlassAndTaxonomiesToAdd(Map<String, Object> requestMap, Vertex rule,
      List<String> klassIdsToAdd, List<String> taxonomyIdsToAdd) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForSwitchTypeRequestModel.TAXONOMY_IDS);
    
    Iterator<Vertex> klassIntermediateIterator = rule
        .getVertices(Direction.OUT, RelationshipLabelConstants.TYPE_NORMALIZATION_LINK)
        .iterator();
    Vertex klassNormalizationintermediate = getNormalizationNodeFromIterator(
        klassIntermediateIterator);
    if (klassNormalizationintermediate != null) {
      Iterable<Vertex> linkedKlassVertices = klassNormalizationintermediate
          .getVertices(Direction.OUT, RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
      for (Vertex linkedKlass : linkedKlassVertices) {
        String linkedKlassId = UtilClass.getCodeNew(linkedKlass);
        if (!klassIds.contains(linkedKlassId) && !klassIdsToAdd.contains(linkedKlassId)) {
          klassIdsToAdd.add(linkedKlassId);
        }
      }
    }
    
    Iterator<Vertex> intermediateIterator = rule
        .getVertices(Direction.OUT, RelationshipLabelConstants.TAXONOMY_NORMALIZATION_LINK)
        .iterator();
    Vertex intermediateNode = getNormalizationNodeFromIterator(intermediateIterator);
    if (intermediateNode != null) {
      Iterable<Vertex> linkedTaxonomyVertices = intermediateNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
      for (Vertex linkedTaxonomy : linkedTaxonomyVertices) {
        String linkedTaxonomyId = UtilClass.getCodeNew(linkedTaxonomy);
        if (!taxonomyIds.contains(linkedTaxonomyId) /*&& !addedTaxonomyId.equals(linkedTaxonomyId)*/
            && !taxonomyIdsToAdd.contains(linkedTaxonomyId)) {
          taxonomyIdsToAdd.add(linkedTaxonomyId);
        }
      }
    }
  }
  
  private void fillParentTagDetail(Map<String, Object> taxonomyMap, Vertex taxonomyVertex)
      throws MultipleLinkFoundException
  {
    Vertex parentTag = TagUtils.getParentTag(taxonomyVertex);
    if (parentTag != null) {
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT_TAG_ID,
          UtilClass.getCodeNew(parentTag));
    }
  }
}
