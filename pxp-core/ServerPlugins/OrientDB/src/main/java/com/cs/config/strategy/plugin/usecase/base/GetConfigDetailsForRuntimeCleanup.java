package com.cs.config.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.IRuntimeCleanupModel;
import com.cs.core.config.plugin.ConfigDetailsRuntimeCleanupHelperModel;
import com.cs.core.config.plugin.IConfigDetailsRuntimeCleanupHelperModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForRuntimeCleanUpResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForRuntimeCleanup extends AbstractOrientPlugin {

  public GetConfigDetailsForRuntimeCleanup(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForRuntimeCleanup/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    IConfigDetailsRuntimeCleanupHelperModel configHelperModel = new ConfigDetailsRuntimeCleanupHelperModel();
    Map<String, Object> mapToReturn = getMapToReturn(configHelperModel);
    
    fillApplicableDetails(requestMap, configHelperModel);
    fillRemovedDetails(requestMap, configHelperModel);
    removeApplicableDetails(configHelperModel);
    fillReferencedRelationships(configHelperModel);
    mapToReturn.put(IConfigDetailsForRuntimeCleanUpResponseModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, configHelperModel.getNumberOfVersionsToMaintain());
    return mapToReturn;
  }

  /**
   * @param configHelperModel
   */
  private void removeApplicableDetails(IConfigDetailsRuntimeCleanupHelperModel configHelperModel)
  {
    configHelperModel.getRemovedTaskIds().removeAll(configHelperModel.getApplicableTaskIds());
    configHelperModel.getRemovedRelationshipSideIds().removeAll(configHelperModel.getApplicableRelationshipSideIds());
    configHelperModel.getRemovedContextIds().removeAll(configHelperModel.getApplicableContextIds());
    Map<String, List<String>> removedAttributeIdVsContextIds = configHelperModel.getRemovedAttributeIdVsContextIds();
    Map<String, List<String>> applicableAttributeIdVsContextIds = configHelperModel.getApplicableAttributeIdVsContextIds();
    for (String attributeId : applicableAttributeIdVsContextIds.keySet()) {
      List<String> attributeContextIds = removedAttributeIdVsContextIds.get(attributeId);
      if(attributeContextIds != null) {
        attributeContextIds.removeAll(applicableAttributeIdVsContextIds.get(attributeId));
      }
    }
  }

  /**
   * @param requestMap
   * @param configHelperModel
   * @throws Exception
   */
  public static void fillApplicableDetails(Map<String, Object> requestMap,
      IConfigDetailsRuntimeCleanupHelperModel configHelperModel) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IRuntimeCleanupModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(IRuntimeCleanupModel.TAXONOMY_IDS);
    
    fillApplicableDetails(configHelperModel, klassIds, taxonomyIds);
  }

  public static void fillApplicableDetails(
      IConfigDetailsRuntimeCleanupHelperModel configHelperModel, List<String> klassIds,
      List<String> taxonomyIds) throws Exception
  {
    List<String> klassAndTaxonomyIds = new ArrayList<String>();
    Set<String> taskIds = configHelperModel.getApplicableTaskIds();
    Set<String> contextIds = configHelperModel.getApplicableContextIds();
    Set<String> relationshipSideIds = configHelperModel.getApplicableRelationshipSideIds();
    Map<String, List<String>> attributeIdVsContextIds = configHelperModel.getApplicableAttributeIdVsContextIds();
    
    for (String klassId : klassIds) {
      Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
      if(isNature != null && isNature) {
        Integer numberOfVersionsToMaintain = klassVertex.getProperty(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        configHelperModel.setNumberOfVersionsToMaintain(numberOfVersionsToMaintain);
      }
      klassAndTaxonomyIds.add(klassVertex.getId().toString());
      fillTasks(klassVertex, taskIds);
      fillContextIds(klassVertex, contextIds);
    }
    
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      klassAndTaxonomyIds.add(taxonomyVertex.getId().toString());
      fillTasks(taxonomyVertex, taskIds);
      fillContextIds(taxonomyVertex, contextIds);
    }
    
    fillRelationshipSideIds(klassAndTaxonomyIds, relationshipSideIds);
    fillAttributeContextIds(klassAndTaxonomyIds, attributeIdVsContextIds);
  }
  
  private static void fillAttributeContextIds(List<String> klassAndTaxonomyIds,
      Map<String, List<String>> attributeIdVsContextIds)
  {
    String query = "select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')["
        + IRelationshipSide.TYPE  + "='" + CommonConstants.ATTRIBUTE + "']) from  "
        + klassAndTaxonomyIds.toString();
    
    Iterable<Vertex> verticesFromQuery = UtilClass.getVerticesFromQuery(query);
    for (Vertex klassAttributeVertex : verticesFromQuery) {
      Iterable<Vertex> variantContextOfVertices = klassAttributeVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      List<String> variantContextIds = new ArrayList<String>();
      for (Vertex attributeVariantNode : variantContextOfVertices) {
        variantContextIds.add((String) attributeVariantNode.getProperty(CommonConstants.CID_PROPERTY));
      }
      
      if (!variantContextIds.isEmpty()) {
        String attributeId = klassAttributeVertex.getProperty(CommonConstants.PROPERTY_ID);
        List<String> attributeVariantIds = attributeIdVsContextIds.get(attributeId);
        if (attributeVariantIds != null) {
          attributeVariantIds.addAll(variantContextIds);
        }
        else {
          attributeIdVsContextIds.put(attributeId, variantContextIds);
        }
      }
    }
    
  }

  /**
   * @param requestMap
   * @param configHelperModel
   * @throws Exception
   */
  private void fillRemovedDetails(Map<String, Object> requestMap,
      IConfigDetailsRuntimeCleanupHelperModel configHelperModel) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(IRuntimeCleanupModel.REMOVED_KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(IRuntimeCleanupModel.REMOVED_TAXONOMY_IDS);
    
    List<String> klassAndTaxonomyIds = new ArrayList<String>();
    Set<String> taskIds = configHelperModel.getRemovedTaskIds();
    Set<String> contextIds = configHelperModel.getRemovedContextIds();
    Set<String> relationshipSideIds = configHelperModel.getRemovedRelationshipSideIds();
    Map<String, List<String>> attributeIdVsContextIds = configHelperModel.getRemovedAttributeIdVsContextIds();
    
    for (String klassId : klassIds) {
      Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      klassAndTaxonomyIds.add(klassVertex.getId().toString());
      fillTasks(klassVertex, taskIds);
      fillContextIds(klassVertex, contextIds);
    }
    
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      klassAndTaxonomyIds.add(taxonomyVertex.getId().toString());
      fillTasks(taxonomyVertex, taskIds);
      fillContextIds(taxonomyVertex, contextIds);
    }
    
    fillRelationshipSideIds(klassAndTaxonomyIds, relationshipSideIds);
    fillAttributeContextIds(klassAndTaxonomyIds, attributeIdVsContextIds);
  }
  
  /**
   * @param klassAndTaxonomyIds
   * @param relationshipSideIds
   * @description: This is used for nature and non-nature relationship
   */
  private static void fillRelationshipSideIds(List<String> klassAndTaxonomyIds,
      Set<String> relationshipSideIds)
  {
    String query = "select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')[" + IRelationshipSide.TYPE + "='"
        + CommonConstants.RELATIONSHIP + "'][" + IKlass.IS_NATURE + "=False]) from  " + klassAndTaxonomyIds.toString();
    
    Iterable<Vertex> verticesFromQuery = UtilClass.getVerticesFromQuery(query);
    for (Vertex relationshipSideVertex : verticesFromQuery) {
      relationshipSideIds.add(UtilClass.getCId(relationshipSideVertex));
    }
  }

  /**
   * @param klassVertex
   * @param applicableTaskIds
   * @param applicableEventIds
   */
  private static void fillTasks(Vertex klassVertex, Set<String> applicableTaskIds)
  {
    Iterable<Vertex> taskVertices = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Vertex taskVertex : taskVertices) {
      applicableTaskIds.add(UtilClass.getCId(taskVertex));
    }
  }
  
  private static void fillContextIds(Vertex klassVertex, Set<String> applicableContextIds)
  {
    Iterable<Vertex> contextKlassIterable = klassVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      Iterator<Vertex> variantContextsIterator = contextKlassNode.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
      Vertex variantContextNode = variantContextsIterator.next();
      applicableContextIds.add(UtilClass.getCId(variantContextNode));
    }
  }
  
  private Map<String,Object> getMapToReturn(IConfigDetailsRuntimeCleanupHelperModel configHelperModel)
  {
    Map<String,Object> returnMap = new HashMap<>();
    returnMap.put(IConfigDetailsForRuntimeCleanUpResponseModel.TASK_IDS, configHelperModel.getRemovedTaskIds());
    returnMap.put(IConfigDetailsForRuntimeCleanUpResponseModel.CONTEXT_IDS, configHelperModel.getRemovedContextIds());
    returnMap.put(IConfigDetailsForRuntimeCleanUpResponseModel.ATTRIBUTE_ID_VS_CONTEXT_IDS, configHelperModel.getRemovedAttributeIdVsContextIds());
    returnMap.put(IConfigDetailsForRuntimeCleanUpResponseModel.RELATIONSHIP_SIDE_ID_VS_REFERENCED_RELATION, configHelperModel.getRelationshipSideIdVsReferencedRelationships());
    
    return returnMap;
  }
  
  private void fillReferencedRelationships(IConfigDetailsRuntimeCleanupHelperModel configHelperModel) throws Exception
  {
    Set<String> removedRelationshipSideIds = configHelperModel.getRemovedRelationshipSideIds();
    if(removedRelationshipSideIds.isEmpty()) {
      return;
    }

    Map<String, Object> relSideVsreferencedRelationships = configHelperModel.getRelationshipSideIdVsReferencedRelationships();
    
    String query = "select expand(out('" + RelationshipLabelConstants.HAS_PROPERTY + "')) from "
        + "( select from " + VertexLabelConstants.KLASS_RELATIONSHIP + " where " + CommonConstants.CODE_PROPERTY 
        + " in " + EntityUtil.quoteIt(removedRelationshipSideIds) + ")";

    Iterable<Vertex> relationshipVertices = UtilClass.getVerticesFromQuery(query);
    for (Vertex relationship : relationshipVertices) {
      Map<String, Object> side1 = relationship.getProperty(IRelationship.SIDE1);
      String side1ElementId = (String) side1.get(IRelationshipSide.ELEMENT_ID);
      Map<String, Object> side2 = relationship.getProperty(IRelationship.SIDE2); 
      String side2ElementId = (String) side2.get(IRelationshipSide.ELEMENT_ID);

      fillRelationshipDetails(side1ElementId, IRelationship.SIDE1, relationship,
          relSideVsreferencedRelationships, removedRelationshipSideIds);
      fillRelationshipDetails(side2ElementId, IRelationship.SIDE2, relationship,
          relSideVsreferencedRelationships, removedRelationshipSideIds);
    }
  }
  
  protected void fillRelationshipDetails(String sideElementId, String side, Vertex relationship,
      Map<String, Object> relSideVsreferencedRelationships, Set<String> removedRelationshipSideIds)
      throws Exception
  {
    if (removedRelationshipSideIds.contains(sideElementId)
        && !relSideVsreferencedRelationships.containsKey(sideElementId)) {
      Map<String, Object> relationshipMap = new HashMap<String, Object>();
      
      relationshipMap.put(CommonConstants.CODE_PROPERTY,
          relationship.getProperty(CommonConstants.CODE_PROPERTY));
      relationshipMap.put(CommonConstants.PROPERTY_IID,
          relationship.getProperty(CommonConstants.PROPERTY_IID));
      relationshipMap.put(CommonConstants.SIDE_PROPERTY, side);
      
      relSideVsreferencedRelationships.put(sideElementId, relationshipMap);
    }
  }
}