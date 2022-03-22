package com.cs.config.strategy.plugin.usecase.authorization.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.processevent.IGetConfigDetailsModelForProcess;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class AuthorizationUtils {
  
  public static void saveAttributeMapping(Vertex profileNode, List<String> addedAttributesMapping,
      List<String> deletedAttributesMapping) throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedAttributesMapping,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE);
    if (!deletedAttributesMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
          RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE, deletedAttributesMapping,
          (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveTagMapping(Vertex profileNode, List<String> addedTagsMapping,
      List<String> deletedTagsMapping) throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedTagsMapping,
        VertexLabelConstants.ENTITY_TAG, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG);
    if (!deletedTagsMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.ENTITY_TAG, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG,
          deletedTagsMapping, (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveClassMapping(Vertex profileNode, List<String> addedClassesMapping,
      List<String> deletedClassesMapping) throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedClassesMapping,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS);
    if (!deletedClassesMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
          RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS, deletedClassesMapping,
          (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveTaxonomyMapping(Vertex profileNode, List<String> addedTaxonomiesMapping,
      List<String> deletedTaxonomiesMapping) throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedTaxonomiesMapping,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES);
    if (!deletedTaxonomiesMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.ROOT_KLASS_TAXONOMY,
          RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES, deletedTaxonomiesMapping,
          (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveContextMapping(Vertex profileNode, List<String> addedContextMapping,
      List<String> deletedContextMapping) throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedContextMapping,
        VertexLabelConstants.VARIANT_CONTEXT,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT);
    if (!deletedContextMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.VARIANT_CONTEXT, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT,
          deletedContextMapping, (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveRelationshipMapping(Vertex profileNode,
      List<String> addedRelationshipMapping, List<String> deletedRelationshipMapping)
      throws Exception
  {
    AuthorizationUtils.addEntityMappings(profileNode, addedRelationshipMapping,
        VertexLabelConstants.ROOT_RELATIONSHIP,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP);
    if (!deletedRelationshipMapping.isEmpty()) {
      AuthorizationUtils.deleteEntityMappings(VertexLabelConstants.ROOT_RELATIONSHIP,
          RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP, deletedRelationshipMapping,
          (String) profileNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void saveMapping(Vertex profileNode, Map<String, Object> profileMap)
      throws Exception
  {
    List<String> addedAttributesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_ATRRIBUTE_MAPPINGS);
    List<String> deletedAttributesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_ATTRIBUTE_MAPPINGS);
    saveAttributeMapping(profileNode, addedAttributesMapping, deletedAttributesMapping);
    
    List<String> addedTagsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_TAG_MAPPINGS);
    List<String> deletedTagsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_TAG_MAPPINGS);
    saveTagMapping(profileNode, addedTagsMapping, deletedTagsMapping);
    
    List<String> addedClassesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_CLASS_MAPPINGS);
    List<String> deletedClassesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_CLASS_MAPPINGS);
    saveClassMapping(profileNode, addedClassesMapping, deletedClassesMapping);
    
    List<String> addedTaxonomiesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_TAXONOMY_MAPPINGS);
    List<String> deletedTaxonomiesMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_TAXONOMY_MAPPINGS);
    saveTaxonomyMapping(profileNode, addedTaxonomiesMapping, deletedTaxonomiesMapping);
    
    List<String> addedContextsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_CONTEXT_MAPPINGS);
    List<String> deletedContextsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_CONTEXT_MAPPINGS);
    saveContextMapping(profileNode, addedContextsMapping, deletedContextsMapping);
    
    List<String> addedRelationshipsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.ADDED_RELATIONSHIP_MAPPINGS);
    List<String> deletedRelationshipsMapping = (List<String>) profileMap
        .get(ISavePartnerAuthorizationModel.DELETED_RELATIONSHIP_MAPPINGS);
    saveRelationshipMapping(profileNode, addedRelationshipsMapping, deletedRelationshipsMapping);
  }
  
  public static void addEntityMappings(Vertex profileNode, List<String> addedEntitiesMapping,
      String entityType, String relationshipLabel) throws Exception
  {
    
    for (String addedEntityMapping : addedEntitiesMapping) {
      
      Vertex attributeNode = UtilClass.getVertexById(addedEntityMapping, entityType);
      
      profileNode.addEdge(relationshipLabel, attributeNode);
    }
  }
  
  public static void deleteEntityMappings(String entityType, String relationshipLabel,
      List<String> deletedEntitiesMapping, String enitityCode) throws Exception
  {
    
    for (String deletedEntityMappingId : deletedEntitiesMapping) {
      Vertex entityVertex = UtilClass.getVertexById(deletedEntityMappingId, entityType);
      Iterable<Edge> edges = entityVertex.getEdges(Direction.IN, relationshipLabel);
      
      for (Edge edge : edges) {
        if (edge.getVertex(Direction.OUT).getProperty(CommonConstants.CODE_PROPERTY).equals(enitityCode)) {
          edge.remove();
        }
      }
    }
  }
  
  public static List<String> getEntityMappings(Vertex profileNode, String relationshipLabel)
      throws Exception
  {
    List<String> ids = new ArrayList<String>();
    Iterable<Vertex> authorizationEntityNodes = profileNode.getVertices(Direction.OUT,
        relationshipLabel);
    for (Vertex authorizationEntityNode : authorizationEntityNodes) {
      ids.add((String) UtilClass.getValueByLanguage(authorizationEntityNode,
          CommonConstants.CODE_PROPERTY));
    
    }
    return ids;
  }
  
  public static List<String> getEntityMappingsWitChild(Vertex profileNode, String relationshipLabel)
      throws Exception
  {
    List<String> ids = new ArrayList<String>();
    Iterable<Vertex> authorizationEntityNodes = profileNode.getVertices(Direction.OUT,
        relationshipLabel);
    for (Vertex authorizationEntityNode : authorizationEntityNodes) {
       ids.add((String) UtilClass.getValueByLanguage(authorizationEntityNode,
          CommonConstants.CODE_PROPERTY));
       if (relationshipLabel.equals(RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES)) {
        fillTaxonomiesChildrenData(ids, authorizationEntityNode);
      }
      
    }
    return ids;
  }
  protected static void fillTaxonomiesChildrenData(List<String> ids, Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId().toString();
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    for (Vertex childNode : resultIterable) {
      ids.add((String) UtilClass.getMapFromVertex(fieldsToFetch, childNode)
          .get(CommonConstants.CODE_PROPERTY));
      
    }
    
  }
  public static Map<String, Object> getReferencedDetails(Vertex profileNode,
      String relationshipLabel) throws Exception
  {
    String id;
    Map<String, Object> referenceMap = new HashMap<String, Object>();
    Iterable<Vertex> authorizationEntityNodes = profileNode.getVertices(Direction.OUT,
        relationshipLabel);
    for (Vertex authorizationEntityNode : authorizationEntityNodes) {
      
      id = (String) UtilClass.getValueByLanguage(authorizationEntityNode,
          CommonConstants.CODE_PROPERTY);
      List<String> fieldsToInclude = new ArrayList<String>();
      fieldsToInclude.add(CommonConstants.CODE_PROPERTY);
      fieldsToInclude.add(CommonConstants.LABEL_PROPERTY);
      fieldsToInclude.add(CommonConstants.CODE_PROPERTY);
      Map<String, Object> nodeMap = UtilClass.getMapFromVertex(fieldsToInclude,
          authorizationEntityNode);
      referenceMap.put(id, nodeMap);
    }
    return referenceMap;
  }
  
  public static Map<String, Object> getReferencedDetailsForTaxonomies(Vertex profileNode,
      String relationshipLabel)
  {
    Map<String, Object> referenceMap = new HashMap<String, Object>();
    Iterable<Vertex> taxonomyNodes = profileNode.getVertices(Direction.OUT, relationshipLabel);
    for (Vertex taxonomy : taxonomyNodes) {
      String taxonomyId = UtilClass.getCodeNew(taxonomy);
      Map<String, Object> taxonomyMap = new HashMap<>();
      
      try {
        TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomy);
        referenceMap.put(taxonomyId, taxonomyMap);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return referenceMap;
  }
  
  public static void getEntityMapFromAuthorizationMappingNode(Vertex profileNode,
      Map<String, Object> entityMap) throws Exception
  {
    entityMap.put(IPartnerAuthorizationModel.ATTRIBUTE_MAPPINGS,
        getEntityMappings(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE));
    entityMap.put(IPartnerAuthorizationModel.TAG_MAPPINGS,
        getEntityMappings(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG));
    entityMap.put(IPartnerAuthorizationModel.CLASS_MAPPINGS,
        getEntityMappings(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS));
    entityMap.put(IPartnerAuthorizationModel.TAXONOMY_MAPPINGS,
        getEntityMappings(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES));
    entityMap.put(IPartnerAuthorizationModel.CONTEXT_MAPPINGS,
        getEntityMappings(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT));
    entityMap.put(IPartnerAuthorizationModel.RELATIONSHIP_MAPPINGS, getEntityMappings(profileNode,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP));
  }
  
  public static void getEntityMapFromAuthorizationMappingWithChildNode(Vertex profileNode,
      Map<String, Object> entityMap) throws Exception
  {
    entityMap.put(IPartnerAuthorizationModel.ATTRIBUTE_MAPPINGS,
        getEntityMappingsWitChild(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE));
    entityMap.put(IPartnerAuthorizationModel.TAG_MAPPINGS,
        getEntityMappingsWitChild(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG));
    entityMap.put(IPartnerAuthorizationModel.CLASS_MAPPINGS,
        getEntityMappingsWitChild(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS));
    entityMap.put(IPartnerAuthorizationModel.TAXONOMY_MAPPINGS,
        getEntityMappingsWitChild(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES));
    entityMap.put(IPartnerAuthorizationModel.CONTEXT_MAPPINGS,
        getEntityMappingsWitChild(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT));
    entityMap.put(IPartnerAuthorizationModel.RELATIONSHIP_MAPPINGS, getEntityMappings(profileNode,
        RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP));
  }
  
   public static void getConfigDetails(Vertex profileNode, Map<String, Object> ConfigDetailsMap)
      throws Exception
  {
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES,
        getReferencedDetails(profileNode,
            RelationshipLabelConstants.HAS_AUTHORIZATION_TO_ATTRIBUTE));
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TAGS,
        getReferencedDetails(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAG));
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES,
        getReferencedDetails(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_KLASS));
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_CONTEXTS,
        getReferencedDetails(profileNode, RelationshipLabelConstants.HAS_AUTHORIZATION_TO_CONTEXT));
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_RELATIONSHIPS,
        getReferencedDetails(profileNode,
            RelationshipLabelConstants.HAS_AUTHORIZATION_TO_RELATIONSHIP));
    ConfigDetailsMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TAXONOMIES,
        getReferencedDetails(profileNode,
            RelationshipLabelConstants.HAS_AUTHORIZATION_TO_TAXONOMIES));
  }
}
