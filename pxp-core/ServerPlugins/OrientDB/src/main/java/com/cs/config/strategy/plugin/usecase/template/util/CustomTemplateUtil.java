package com.cs.config.strategy.plugin.usecase.template.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.customtemplate.ICustomTemplate;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IConfigDetailsForGridTemplatesModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTemplateUtil {
  
  public static final List<String> fieldsToFetchForTemplateRefencedProperties = Arrays
      .asList(CommonConstants.CODE_PROPERTY, IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE);
  
  // This method wont be used as single save call wont be used from UI. only
  // bulk save templates
  @Deprecated
  public static final Map<String, Object> prepareTemplateResponseMap(Vertex templateNode)
      throws Exception
  {
    List<String> fieldToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ICustomTemplate.LABEL, ICustomTemplate.ICON, ICustomTemplate.CODE);
    Map<String, Object> templateMap = UtilClass.getMapFromVertex(fieldToFetch, templateNode);
    
    List<String> pCIds = new ArrayList<>();
    List<String> relationshipIds = new ArrayList<>();
    List<String> natureRelationshipIds = new ArrayList<>();
    List<String> contextIds = new ArrayList<>();
    templateMap.put(ICustomTemplate.PROPERTY_COLLECTION_IDS, pCIds);
    templateMap.put(ICustomTemplate.RELATIONSHIP_IDS, relationshipIds);
    templateMap.put(ICustomTemplate.NATURE_RELATIONSHIP_IDS, natureRelationshipIds);
    templateMap.put(ICustomTemplate.CONTEXT_IDS, contextIds);
 
    
    Map<String, Object> referencedPCs = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedContexts = new HashMap<>();
    
    fillPropertyCollectionForTemplate(templateNode, pCIds, referencedPCs);
    fillRelationshipForTemplate(templateNode, relationshipIds, referencedRelationships);
    fillNatureRelationshipForTemplate(templateNode, natureRelationshipIds,
        referencedNatureRelationships);
    fillContextForTemplate(templateNode, contextIds, referencedContexts);
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(IGetCustomTemplateModel.TEMPLATE, templateMap);
    responseMap.put(IGetCustomTemplateModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPCs);
    responseMap.put(IGetCustomTemplateModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    responseMap.put(IGetCustomTemplateModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    responseMap.put(IGetCustomTemplateModel.REFERENCED_CONTEXT, referencedContexts);
    return responseMap;
  }
  
  @SuppressWarnings("unchecked")
  public static void prepareTemplateResponseForGridTemplatesMap(Vertex templateNode,
      Map<String, Object> configDetails, List<Map<String, Object>> returnList) throws Exception
  {
    List<String> fieldToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ICustomTemplate.LABEL, ICustomTemplate.ICON, ICustomTemplate.CODE, ICustomTemplate.TYPE);
    Map<String, Object> templateMap = UtilClass.getMapFromVertex(fieldToFetch, templateNode);
    
    List<String> pCIds = new ArrayList<>();
    List<String> relationshipIds = new ArrayList<>();
    List<String> natureRelationshipIds = new ArrayList<>();
    List<String> contextIds = new ArrayList<>();
    templateMap.put(ICustomTemplate.PROPERTY_COLLECTION_IDS, pCIds);
    templateMap.put(ICustomTemplate.RELATIONSHIP_IDS, relationshipIds);
    templateMap.put(ICustomTemplate.NATURE_RELATIONSHIP_IDS, natureRelationshipIds);
    templateMap.put(ICustomTemplate.CONTEXT_IDS, contextIds);
    returnList.add(templateMap);
    
    Map<String, Object> referencedPCs = (Map<String, Object>) configDetails
        .get(IConfigDetailsForGridTemplatesModel.REFERENCED_PROPERTY_COLLECTIONS);
    Map<String, Object> referencedRelationships = (Map<String, Object>) configDetails
        .get(IConfigDetailsForGridTemplatesModel.REFERENCED_RELATIONSHIPS);
    Map<String, Object> referencedNatureRelationships = (Map<String, Object>) configDetails
        .get(IConfigDetailsForGridTemplatesModel.REFERENCED_NATURE_RELATIONSHIPS);
    Map<String, Object> referencedContexts = (Map<String, Object>) configDetails
        .get(IConfigDetailsForGridTemplatesModel.REFERENCED_CONTEXT);
    
    fillPropertyCollectionForTemplate(templateNode, pCIds, referencedPCs);
    fillRelationshipForTemplate(templateNode, relationshipIds, referencedRelationships);
    fillNatureRelationshipForTemplate(templateNode, natureRelationshipIds,
        referencedNatureRelationships);
    fillContextForTemplate(templateNode, contextIds, referencedContexts);
  }
  
  private static void fillPropertyCollectionForTemplate(Vertex templateNode, List<String> pCIds,
      Map<String, Object> referencedPCs)
  {
    Iterable<Vertex> vertices = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION);
    for (Vertex vertex : vertices) {
      Map<String, Object> map = UtilClass
          .getMapFromVertex(fieldsToFetchForTemplateRefencedProperties, vertex);
      String id = (String) map.get(IIdLabelModel.ID);
      pCIds.add(id);
      referencedPCs.put(id, map);
    }
  }
  
  private static void fillRelationshipForTemplate(Vertex templateNode, List<String> relationshipIds,
      Map<String, Object> referencedRelationships)
  {
    Iterable<Vertex> vertices = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP);
    for (Vertex vertex : vertices) {
      Map<String, Object> map = UtilClass
          .getMapFromVertex(fieldsToFetchForTemplateRefencedProperties, vertex);
      String id = (String) map.get(IIdLabelModel.ID);
      relationshipIds.add(id);
      referencedRelationships.put(id, map);
    }
  }
  
  private static void fillNatureRelationshipForTemplate(Vertex templateNode,
      List<String> natureRelationshipIds, Map<String, Object> referencedNatureRelationships)
  {
    Iterable<Vertex> vertices = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP);
    for (Vertex vertex : vertices) {
      Map<String, Object> map = UtilClass
          .getMapFromVertex(fieldsToFetchForTemplateRefencedProperties, vertex);
      String id = (String) map.get(IIdLabelModel.ID);
      natureRelationshipIds.add(id);
      referencedNatureRelationships.put(id, map);
    }
  }
  
  private static void fillContextForTemplate(Vertex templateNode, List<String> contextIds,
      Map<String, Object> referencedContexts)
  {
    Iterable<Vertex> vertices = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT);
    for (Vertex vertex : vertices) {
      Map<String, Object> map = UtilClass
          .getMapFromVertex(fieldsToFetchForTemplateRefencedProperties, vertex);
      String id = (String) map.get(IIdLabelModel.ID);
      contextIds.add(id);
      referencedContexts.put(id, map);
    }
  }
}
