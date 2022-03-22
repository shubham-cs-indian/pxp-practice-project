package com.cs.config.strategy.plugin.usecase.relationship.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.ReferencedContextUtil;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ReferencedRelationshipUtil {
  
  /**
   * @author Aayush
   * @param helperObject
   * @param relationshipType
   * @return
   */
  public static Map<String, Object> getReferencedRelationships(Map<String, Object> helperObject,
      Boolean isNatureRelationship)
  {
    if (isNatureRelationship == null || !isNatureRelationship) {
      return (Map<String, Object>) helperObject
          .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS);
    }
    else {
      return (Map<String, Object>) helperObject
          .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_NATURE_RELATIONSHIPS);
    }
  }
  
  /**
   * @author Aayush
   * @param mapToReturn
   * @param relationshipNode
   * @param relationshipId
   * @throws KlassNotFoundException
   * @throws Exception
   */
  public static void fillReferencedRelationshipsProperties(Map<String, Object> mapToReturn,
      Vertex relationshipNode, String relationshipId) throws KlassNotFoundException, Exception
  {
    String label = (String) UtilClass.getValueByLanguage(relationshipNode,
        CommonConstants.LABEL_PROPERTY);
    Map<String, Object> relationshipPropertiesMap = new HashMap<>();
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.CODE,
        relationshipNode.getProperty(CommonConstants.CODE_PROPERTY));
    
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
    referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
  }
  
  /**
   * fill referenced relationship map with context details
   *
   * @author Aayush
   * @param relationshipNode
   * @param helperObject
   * @throws Exception
   */
  public static void fillReferencedRelationship(Vertex relationshipNode,
      Map<String, Object> helperObject) throws Exception
  {
    Boolean isNatureRelationship = relationshipNode.getProperty(IRelationship.IS_NATURE);
    Map<String, Object> referencedRelationshipMap = ReferencedRelationshipUtil
        .getReferencedRelationships(helperObject, isNatureRelationship);
    
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    HashMap<String, Object> relationshipMap = RelationshipUtils
        .getRelationshipMap(relationshipNode);
    referencedRelationshipMap.put(relationshipId, relationshipMap);
    
    Map<String, Object> referencedContextsMap = (Map<String, Object>) helperObject
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTags = (Map<String, Object>) helperObject
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    fillRelationshipContextDetails(relationshipNode, relationshipMap, referencedContextsMap,
        referencedTags);
    Map<String, Object> referencedElements = (Map<String, Object>) helperObject
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    fillReferencedRelationshipElements(relationshipNode, referencedElements);
  }
  
  /**
   * @author Aayush
   * @param relationshipNode
   * @param referencedElements
   */
  public static void fillReferencedRelationshipElements(Vertex relationshipNode,
      Map<String, Object> referencedElements)
  {
    Iterable<Vertex> klassRelationshipNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassRelationshipNode : klassRelationshipNodes) {
      HashMap<String, Object> klassRelationship = UtilClass.getMapFromNode(klassRelationshipNode);
      referencedElements.put(UtilClass.getCodeNew(klassRelationshipNode), klassRelationship);
      
      // add klass or taxonomy Ids
      Iterable<Vertex> klassesOrTaxonomiesNode = klassRelationshipNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      List<String> klassOrTaxonomyIds = new ArrayList<>();
      for (Vertex klassOrTaxonomyNode : klassesOrTaxonomiesNode) {
        klassOrTaxonomyIds.add(UtilClass.getCodeNew(klassOrTaxonomyNode));
      }
      klassRelationship.put(IReferencedSectionRelationshipModel.VERSION_TIMESTAMP,
          klassOrTaxonomyIds);
    }
  }
  
  /**
   * 1. sets contextId in the relationshipSide 2. fills referenced Relationship
   * Variant Contexts
   *
   * @author Aayush
   * @param relationshipNode
   * @param relationshipMap
   * @param referencedContexts
   * @throws Exception
   */
  public static void fillRelationshipContextDetails(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Object> referencedContexts,
      Map<String, Object> referencedTags) throws Exception
  {
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    
    Map<String, Object> variantContexts = new HashMap<>();
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassRelationshipNode : kRNodes) {
      Boolean isNature = klassRelationshipNode.getProperty(IKlassNatureRelationship.IS_NATURE);
      Vertex context = null;
      if (isNature == null || !isNature) {
        context = RelationshipDBUtil.getContextFromKlassRelationshipVertex(klassRelationshipNode);
        variantContexts = (Map<String, Object>) referencedContexts
            .get(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS);
      }
      else {
        context = RelationshipDBUtil
            .getLinkedVariantContextFromKlassRelationshipVertex(klassRelationshipNode);
        variantContexts = (Map<String, Object>) referencedContexts
            .get(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS);
      }
      if (context == null) {
        continue;
      }
      String contextId = UtilClass.getCodeNew(context);
      
      String KRSide = klassRelationshipNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(CommonConstants.RELATIONSHIP_SIDE_2)) {
        side2.put(IRelationshipSide.CONTEXT_ID, contextId);
      }
      else {
        side1.put(IRelationshipSide.CONTEXT_ID, contextId);
      }
      
      if (!variantContexts.containsKey(contextId)) {
        Map<String, Object> referencedContextMap = UtilClass.getMapFromVertex(new ArrayList<>(),
            context);
        ReferencedContextUtil.fillReferencedContextTags(context, referencedContextMap,
            referencedTags);
        variantContexts.put(contextId, referencedContextMap);
      }
    }
  }
  
  /**
   * 1. sets contextId in the relationshipSide 2. fills referenced Relationship
   * Variant Contexts
   *
   * @author Aayush
   * @param relationshipNode
   * @param relationshipMap
   * @param referencedVariantContexts
   * @throws Exception
   */
  public static void fillRelationshipContextDetailsForPID_SKU(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Object> referencedVariantContexts,
      Map<String, Object> referencedTags) throws Exception
  {
    String relationshipType = relationshipNode
        .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
    if (relationshipType != null) {
      return;
    }
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    
    Map<String, Object> relationshipVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS);
    
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassRelationshipNode : kRNodes) {
      Boolean isNature = klassRelationshipNode.getProperty(ISectionRelationship.IS_NATURE);
      if (!isNature) {
        continue;
      }
      Vertex context = RelationshipDBUtil
          .getLinkedVariantContextFromKlassRelationshipVertex(klassRelationshipNode);
      String contextId = UtilClass.getCodeNew(context);
      
      String KRSide = klassRelationshipNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(CommonConstants.RELATIONSHIP_SIDE_2)) {
        side2.put(IRelationshipSide.CONTEXT_ID, contextId);
      }
      else {
        side1.put(IRelationshipSide.CONTEXT_ID, contextId);
      }
      
      if (relationshipVariantContexts.containsKey(contextId)) {
        Map<String, Object> referencedContextMap = UtilClass.getMapFromVertex(null, context);
        ReferencedContextUtil.fillReferencedContextTags(context, referencedContextMap,
            referencedTags);
        relationshipVariantContexts.put(contextId, referencedContextMap);
      }
    }
  }
  
  /**
   * @author Aayush
   * @param relationshipNode
   * @param relationshipPropertiesMap
   * @throws KlassNotFoundException
   * @throws Exception
   */
  public static void fillReferencedRelationshipProperties(Vertex relationshipNode,
      Map<String, Object> relationshipPropertiesMap) throws KlassNotFoundException, Exception
  {
    Map<String, Object> side1 = new HashMap<>();
    Map<String, Object> side2 = new HashMap<>();
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE1, side1);
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE2, side2);
    
    Integer count = 0;
    Iterable<Vertex> klassRelationshipNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassRelationshipNode : klassRelationshipNodes) {
      if (count == 0) {
        RelationshipUtils.populatePropertiesForRelationshipSide(klassRelationshipNode, side1);
        count++;
      }
      else if (count == 1) {
        RelationshipUtils.populatePropertiesForRelationshipSide(klassRelationshipNode, side2);
      }
    }
  }
}
