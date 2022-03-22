package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetConfigDetailsHelperModel implements IGetConfigDetailsHelperModel {
  
  protected Set<String>               instanceKlassIds;
  protected Set<String>               entityIds;
  protected Set<String>               variantTemplateIds;
  protected Set<String>               relationshipIds;
  protected Set<Vertex>               nonNatureKlassNodes;
  protected Map<Vertex, String>       natureNodeVsTemplateIdMap;
  protected Map<String, Set<String>>  templateIdVsAssociatedPropertyCollectionIds;
  protected Map<String, Set<String>>  templateIdVsAssociatedPropertyIds;
  protected Vertex                    natureNode;
  protected Map<String, Set<String>>  contextIdVsPropertyIds;
  protected Map<String, Set<String>>  templateIdVsEmbeddedKlassIds;
  protected String                    userId;
  protected Set<Vertex>               taxonomyVertices;
  protected Map<String, String>       taxonomyIdVsParentIdMap;
  protected Map<String, String>       templateIdVsKlassTaxonomyId;
  protected Boolean                   shouldUseTagIdTagValueIdsMap = false;
  protected Map<String, List<String>> tagIdTagValueIdsMap;
  protected Map<String, Set<String>>  typeIdVsAssociatedPropertyIds;
  protected Map<String, Set<Vertex>>  typeIdVsAssociatedPropertyCollectionVertices;
  protected Map<String, Set<Vertex>>  typeIdVsAssociatedRelationshipVertices;
  protected Set<Vertex>               collectionVertices;
  protected Map<String, Set<Vertex>>  typeIdVsAssociatedContextVertices;
  protected Vertex                    requestedTypeVertex;
  protected String                    requestedTypeVertexLabelInfo;
  protected Set<String>               tagsIds;
  protected Set<String>               attributeIds;
  protected Set<String>               roleIds;
  protected Set<String>               associatedSectionIds;
  protected Set<String>               associatedAttributeContextIds;
  protected String                    klassType;
  protected Set<String>               contextTagIds;
  protected String                    organizationId;
  protected String                    endpointId;
  protected String                    physicalCatalogId;
  protected Map<String, String>       klassIdVsContextId;
  protected Set<String>               natureRelationshipIds;
  protected Set<String>               klassNatureRelationshipIds;
  protected Set<String>               klassRelationshipIds;
  protected Map<String, String>       relationshipIdVsTargetType;
  protected List<String>              matchRelationshipTypes;
  protected Map<String, String>       propertyIdVsCouplingType;
  protected Map<String, Map<String, List<String>>> instanceIdVsOtherClassifiers;
  
  public Set<String> getKlassNatureRelationshipIds()
  {
    if (klassNatureRelationshipIds == null) {
      klassNatureRelationshipIds = new HashSet<>();
    }
    return klassNatureRelationshipIds;
  }
  
  public void setKlassNatureRelationshipIds(Set<String> klassNatureRelationshipIds)
  {
    this.klassNatureRelationshipIds = klassNatureRelationshipIds;
  }
  
  public Set<String> getKlassRelationshipIds()
  {
    if (klassRelationshipIds == null) {
      klassRelationshipIds = new HashSet<>();
    }
    return klassRelationshipIds;
  }
  
  public void setKlassRelationshipIds(Set<String> klassRelationshipIds)
  {
    this.klassRelationshipIds = klassRelationshipIds;
  }
  
  @Override
  public Set<String> getInstanceKlassIds()
  {
    if (instanceKlassIds == null) {
      instanceKlassIds = new HashSet<>();
    }
    return instanceKlassIds;
  }
  
  @Override
  public void setInstanceKlassIds(Set<String> instanceKlassIds)
  {
    this.instanceKlassIds = instanceKlassIds;
  }
  
  @Override
  public Map<String, Set<String>> getTemplateIdVsEmbeddedKlassIds()
  {
    if (templateIdVsEmbeddedKlassIds == null) {
      templateIdVsEmbeddedKlassIds = new HashMap<String, Set<String>>();
    }
    return templateIdVsEmbeddedKlassIds;
  }
  
  @Override
  public void setTemplateIdVsEmbeddedKlassIds(Map<String, Set<String>> templateIdVsEmbeddedKlassIds)
  {
    this.templateIdVsEmbeddedKlassIds = templateIdVsEmbeddedKlassIds;
  }
  
  @Override
  public Map<String, Set<String>> getContextIdVsPropertyIds()
  {
    if (contextIdVsPropertyIds == null) {
      contextIdVsPropertyIds = new HashMap<String, Set<String>>();
    }
    return contextIdVsPropertyIds;
  }
  
  @Override
  public void setContextIdVsPropertyIds(Map<String, Set<String>> contextIdVsPropertyIds)
  {
    this.contextIdVsPropertyIds = contextIdVsPropertyIds;
  }
  
  @Override
  public Vertex getNatureNode()
  {
    return natureNode;
  }
  
  @Override
  public void setNatureNode(Vertex natureNode)
  {
    this.natureNode = natureNode;
  }
  
  @Override
  public Set<String> getVariantTemplateIds()
  {
    if (variantTemplateIds == null) {
      variantTemplateIds = new HashSet<>();
    }
    return variantTemplateIds;
  }
  
  @Override
  public void setVariantTemplateIds(Set<String> variantTemplateIds)
  {
    this.variantTemplateIds = variantTemplateIds;
  }
  
  @Override
  public Set<String> getEntityIds()
  {
    if (entityIds == null) {
      entityIds = new HashSet<>();
    }
    return entityIds;
  }
  
  @Override
  public void setEntityIds(Set<String> entityIds)
  {
    this.entityIds = entityIds;
  }
  
  @Override
  public Map<Vertex, String> getNatureNodeVsTemplateIdMap()
  {
    if (natureNodeVsTemplateIdMap == null) {
      natureNodeVsTemplateIdMap = new HashMap<>();
    }
    return natureNodeVsTemplateIdMap;
  }
  
  @Override
  public void setNatureNodeVsTemplateIdMap(Map<Vertex, String> natureNodeVsTemplateIdMap)
  {
    this.natureNodeVsTemplateIdMap = natureNodeVsTemplateIdMap;
  }
  
  @Override
  public Set<Vertex> getNonNatureKlassNodes()
  {
    if (nonNatureKlassNodes == null) {
      nonNatureKlassNodes = new HashSet<>();
    }
    return nonNatureKlassNodes;
  }
  
  @Override
  public void setNonNatureKlassNodes(Set<Vertex> nonNatureKlassNodes)
  {
    this.nonNatureKlassNodes = nonNatureKlassNodes;
  }
  
  @Override
  public Map<String, Set<String>> getTemplateIdVsAssociatedPropertyCollectionIds()
  {
    if (templateIdVsAssociatedPropertyCollectionIds == null) {
      templateIdVsAssociatedPropertyCollectionIds = new HashMap<>();
    }
    return templateIdVsAssociatedPropertyCollectionIds;
  }
  
  @Override
  public void setTemplateIdVsAssociatedPropertyCollectionIds(
      Map<String, Set<String>> templateIdVsAssociatedPropertyCollectionIds)
  {
    this.templateIdVsAssociatedPropertyCollectionIds = templateIdVsAssociatedPropertyCollectionIds;
  }
  
  @Override
  public Set<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new HashSet<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(Set<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public Map<String, Set<String>> getTemplateIdVsAssociatedPropertyIds()
  {
    if (templateIdVsAssociatedPropertyIds == null) {
      templateIdVsAssociatedPropertyIds = new HashMap<>();
    }
    return templateIdVsAssociatedPropertyIds;
  }
  
  @Override
  public void setTemplateIdVsAssociatedPropertyIds(
      Map<String, Set<String>> templateIdVsAssociatedPropertyIds)
  {
    this.templateIdVsAssociatedPropertyIds = templateIdVsAssociatedPropertyIds;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public Set<Vertex> getTaxonomyVertices()
  {
    if (taxonomyVertices == null) {
      taxonomyVertices = new HashSet<>();
    }
    return taxonomyVertices;
  }
  
  @Override
  public void setTaxonomyVertices(Set<Vertex> taxonomyVertices)
  {
    this.taxonomyVertices = taxonomyVertices;
  }
  
  @Override
  public Map<String, String> getTemplateIdVsKlassTaxonomyId()
  {
    if (templateIdVsKlassTaxonomyId == null) {
      templateIdVsKlassTaxonomyId = new HashMap<>();
    }
    return templateIdVsKlassTaxonomyId;
  }
  
  @Override
  public void setTemplateIdVsKlassTaxonomyId(Map<String, String> templateIdVsKlassTaxonomyId)
  {
    this.templateIdVsKlassTaxonomyId = templateIdVsKlassTaxonomyId;
  }
  
  @Override
  public Boolean getShouldUseTagIdTagValueIdsMap()
  {
    return shouldUseTagIdTagValueIdsMap;
  }
  
  @Override
  public void setShouldUseTagIdTagValueIdsMap(Boolean shouldUseTagIdTagValueIdMap)
  {
    this.shouldUseTagIdTagValueIdsMap = shouldUseTagIdTagValueIdMap;
  }
  
  @Override
  public Map<String, List<String>> getTagIdTagValueIdsMap()
  {
    return tagIdTagValueIdsMap;
  }
  
  @Override
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap)
  {
    this.tagIdTagValueIdsMap = tagIdTagValueIdsMap;
  }
  
  @Override
  public Map<String, Set<String>> getTypeIdVsAssociatedPropertyIds()
  {
    if (typeIdVsAssociatedPropertyIds == null) {
      typeIdVsAssociatedPropertyIds = new HashMap<>();
    }
    return typeIdVsAssociatedPropertyIds;
  }
  
  @Override
  public void setTypeIdVsAssociatedPropertyIds(
      Map<String, Set<String>> typeIdVsAssociatedPropertyIds)
  {
    this.typeIdVsAssociatedPropertyIds = typeIdVsAssociatedPropertyIds;
  }
  
  @Override
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedPropertyCollectionVertices()
  {
    if (typeIdVsAssociatedPropertyCollectionVertices == null) {
      typeIdVsAssociatedPropertyCollectionVertices = new HashMap<>();
    }
    return typeIdVsAssociatedPropertyCollectionVertices;
  }
  
  @Override
  public void setTypeIdVsAssociatedPropertyCollectionVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices)
  {
    this.typeIdVsAssociatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices;
  }
  
  @Override
  public void setTypeIdVsAssociatedRelationshipsVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices)
  {
    this.typeIdVsAssociatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices;
  }
  
  @Override
  public Set<Vertex> getCollectionVertices()
  {
    if (collectionVertices == null) {
      collectionVertices = new HashSet<>();
    }
    return collectionVertices;
  }
  
  @Override
  public void setCollectionVertices(Set<Vertex> collectionVertices)
  {
    this.collectionVertices = collectionVertices;
  }
  
  @Override
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedContextVertices()
  {
    if (typeIdVsAssociatedContextVertices == null) {
      typeIdVsAssociatedContextVertices = new HashMap<>();
    }
    return typeIdVsAssociatedContextVertices;
  }
  
  @Override
  public void setTypeIdVsAssociatedContextVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices)
  {
    this.typeIdVsAssociatedContextVertices = typeIdVsAssociatedContextVertices;
  }
  
  @Override
  public Vertex getRequestedTypeVertex()
  {
    return requestedTypeVertex;
  }
  
  @Override
  public void setRequestedTypeVertex(Vertex requestedTypeVertex)
  {
    this.requestedTypeVertex = requestedTypeVertex;
  }
  
  @Override
  public String getRequestedTypeVertexLabelInfo()
  {
    return requestedTypeVertexLabelInfo;
  }
  
  @Override
  public void setRequestedTypeVertexLabelInfo(String requestedTypeVertexLabelInfo)
  {
    this.requestedTypeVertexLabelInfo = requestedTypeVertexLabelInfo;
  }
  
  @Override
  public Set<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new HashSet<>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(Set<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public Set<String> getTagIds()
  {
    if (tagsIds == null) {
      tagsIds = new HashSet<>();
    }
    return tagsIds;
  }
  
  @Override
  public void setTagIds(Set<String> tagIds)
  {
    this.tagsIds = tagIds;
  }
  
  @Override
  public Set<String> getRoleIds()
  {
    if (roleIds == null) {
      roleIds = new HashSet<>();
    }
    return roleIds;
  }
  
  @Override
  public void setRoleIds(Set<String> roleIds)
  {
    this.roleIds = roleIds;
  }
  
  @Override
  public Set<String> getAssociatedSectionIds()
  {
    if (associatedSectionIds == null) {
      associatedSectionIds = new HashSet<>();
    }
    return associatedSectionIds;
  }
  
  @Override
  public void setAssociatedSectionIds(Set<String> associatedSectionIds)
  {
    this.associatedSectionIds = associatedSectionIds;
  }
  
  @Override
  public Set<String> getAssociatedAttributeContextIds()
  {
    if (associatedAttributeContextIds == null) {
      associatedAttributeContextIds = new HashSet<>();
    }
    return associatedAttributeContextIds;
  }
  
  @Override
  public void setAssociatedAttributeContextIds(Set<String> associatedAttributeContextIds)
  {
    this.associatedAttributeContextIds = associatedAttributeContextIds;
  }
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public Set<String> getContextTagIds()
  {
    if (contextTagIds == null) {
      contextTagIds = new HashSet<>();
    }
    return contextTagIds;
  }
  
  @Override
  public void setContextTagIds(Set<String> contextTagIds)
  {
    this.contextTagIds = contextTagIds;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  public Map<String, String> getKlassIdVsContextId()
  {
    if (klassIdVsContextId == null) {
      klassIdVsContextId = new HashMap<>();
    }
    return klassIdVsContextId;
  }
  
  @Override
  public void setKlassIdVsContextId(Map<String, String> klassIdWithRPVsContextId)
  {
    this.klassIdVsContextId = klassIdWithRPVsContextId;
  }
  
  @Override
  public Set<String> getNatureRelationshipIds()
  {
    if (natureRelationshipIds == null) {
      natureRelationshipIds = new HashSet<>();
    }
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(Set<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
  
  @Override
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedRelationshipsVertices()
  {
    if (typeIdVsAssociatedRelationshipVertices == null) {
      typeIdVsAssociatedRelationshipVertices = new HashMap<>();
    }
    return typeIdVsAssociatedRelationshipVertices;
  }

  @Override
  public Map<String, String> getRelationshipIdVsTargetType()
  {
    if (relationshipIdVsTargetType == null) {
      relationshipIdVsTargetType = new HashMap<>();
    }
    return relationshipIdVsTargetType;
  }
  
  @Override
  public void setRelationshipIdVsTargetType(Map<String, String> relationshipSideIdVsTargetType)
  {
    this.relationshipIdVsTargetType = relationshipSideIdVsTargetType;
  }

  @Override
  public List<String> getMatchRelationshipTypes()
  {
    if (matchRelationshipTypes == null) {
      matchRelationshipTypes = new ArrayList<>();
    }
    return matchRelationshipTypes;
  }
  
  @Override
  public void setMatchRelationshipTypes(List<String> matchRelationshipTypes)
  {
    this.matchRelationshipTypes = matchRelationshipTypes;
  }
  @Override
  
  public Map<String, String> getPropertyIdVsCouplingType()
  {
    if (propertyIdVsCouplingType == null) {
      propertyIdVsCouplingType = new HashMap<>();
    }
    return propertyIdVsCouplingType;
  }
  
  @Override
  public void setPropertyIdVsCouplingType(Map<String, String> propertyIdVsCouplingType)
  {
    this.propertyIdVsCouplingType = propertyIdVsCouplingType;
  }
  
  @Override
  public Map<String, Map<String, List<String>>> getInstanceIdVsOtherClassifiers()
  {
    return this.instanceIdVsOtherClassifiers;
  }
  
  @Override
  public void setInstanceIdVsOtherClassifiers(
      Map<String, Map<String, List<String>>> instanceIdVsOtherClassifiers)
  {
    this.instanceIdVsOtherClassifiers = instanceIdVsOtherClassifiers;
  }
}
