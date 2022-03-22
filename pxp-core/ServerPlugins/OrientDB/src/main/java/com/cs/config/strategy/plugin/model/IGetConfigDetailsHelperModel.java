package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetConfigDetailsHelperModel {
  
  public Set<String> getEntityIds();
  
  public void setEntityIds(Set<String> entityIds);
  
  public Set<String> getInstanceKlassIds();
  
  public void setInstanceKlassIds(Set<String> instanceKlassIds);
  
  public Map<Vertex, String> getNatureNodeVsTemplateIdMap();
  
  public void setNatureNodeVsTemplateIdMap(Map<Vertex, String> natureNodeVsTemplateIdMap);
  
  public Set<Vertex> getNonNatureKlassNodes();
  
  public void setNonNatureKlassNodes(Set<Vertex> nonNatureKlassNodes);
  
  public Map<String, Set<String>> getTemplateIdVsAssociatedPropertyCollectionIds();
  
  public void setTemplateIdVsAssociatedPropertyCollectionIds(
      Map<String, Set<String>> templateIdVsAssociatedPropertyCollectionIds);
  
  public Set<String> getVariantTemplateIds();
  
  public void setVariantTemplateIds(Set<String> variantTemplateIds);
  
  public Set<String> getRelationshipIds();
  
  public void setRelationshipIds(Set<String> relationshipIds);
  
  public Set<String> getKlassRelationshipIds();
  
  public void setKlassRelationshipIds(Set<String> klassRelationshipIds);
  
  public Map<String, Set<String>> getTemplateIdVsAssociatedPropertyIds();
  
  public void setTemplateIdVsAssociatedPropertyIds(
      Map<String, Set<String>> templateIdVsAssociatedPropertyIds);
  
  public void setNatureNode(Vertex natureNode);
  
  public Vertex getNatureNode();
  
  // key : attributeContextId
  // value : set of propertyIds linked to key context
  public void setContextIdVsPropertyIds(Map<String, Set<String>> contextIdVsPropertyIds);
  
  public Map<String, Set<String>> getContextIdVsPropertyIds();
  
  // key : templateId
  // value : set of klassIds (GTIN, Embedded, TechnicalImage) linked to
  // the key template
  public Map<String, Set<String>> getTemplateIdVsEmbeddedKlassIds();
  
  public void setTemplateIdVsEmbeddedKlassIds(
      Map<String, Set<String>> templateIdVsEmbeddedKlassIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Set<Vertex> getTaxonomyVertices();
  
  public void setTaxonomyVertices(Set<Vertex> taxonomyVertices);
  
  public Map<String, String> getTemplateIdVsKlassTaxonomyId();
  
  public void setTemplateIdVsKlassTaxonomyId(Map<String, String> templateIdVsKlassTaxonomyId);
  
  public Boolean getShouldUseTagIdTagValueIdsMap();
  
  public void setShouldUseTagIdTagValueIdsMap(Boolean shouldUseTagIdTagValueIdsMap);
  
  public Map<String, List<String>> getTagIdTagValueIdsMap();
  
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap);
  
  public Map<String, Set<String>> getTypeIdVsAssociatedPropertyIds();
  
  public void setTypeIdVsAssociatedPropertyIds(
      Map<String, Set<String>> typeIdVsAssociatedPropertyIds);
  
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedPropertyCollectionVertices();
  
  public void setTypeIdVsAssociatedPropertyCollectionVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices);
  
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedRelationshipsVertices();
  
  public void setTypeIdVsAssociatedRelationshipsVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices);
  
  public Set<Vertex> getCollectionVertices();
  
  public void setCollectionVertices(Set<Vertex> collectionVertices);
  
  public Map<String, Set<Vertex>> getTypeIdVsAssociatedContextVertices();
  
  public void setTypeIdVsAssociatedContextVertices(
      Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices);
  
  public Vertex getRequestedTypeVertex();
  
  public void setRequestedTypeVertex(Vertex requestedTypeVertex);
  
  public String getRequestedTypeVertexLabelInfo();
  
  public void setRequestedTypeVertexLabelInfo(String requestedTypeVertexLabelInfo);
  
  public Set<String> getAttributeIds();
  
  public void setAttributeIds(Set<String> attributeIds);
  
  public Set<String> getTagIds();
  
  public void setTagIds(Set<String> tagIds);
  
  public Set<String> getRoleIds();
  
  public void setRoleIds(Set<String> rolesIds);
  
  public Set<String> getAssociatedSectionIds();
  
  public void setAssociatedSectionIds(Set<String> associatedSectionIds);
  
  public Set<String> getAssociatedAttributeContextIds();
  
  public void setAssociatedAttributeContextIds(Set<String> associatedAttributeContextIds);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public Set<String> getContextTagIds();
  
  public void setContextTagIds(Set<String> contextTagIds);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public Map<String, String> getKlassIdVsContextId();
  
  public void setKlassIdVsContextId(Map<String, String> klassIdVsContextId);
  
  public Set<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(Set<String> natureRelationshipIds);
  
  public Set<String> getKlassNatureRelationshipIds();
  
  public void setKlassNatureRelationshipIds(Set<String> klassNatureRelationshipIds);
  
  public Map<String, String> getRelationshipIdVsTargetType();
  
  public void setRelationshipIdVsTargetType(Map<String, String> relationshipSideIdVsTargetType);
  
  public List<String> getMatchRelationshipTypes();
  
  public void setMatchRelationshipTypes(List<String> matchRelationshipTypes);;
  
  public Map<String, String> getPropertyIdVsCouplingType();
  
  public void setPropertyIdVsCouplingType(Map<String, String> propertyIdVsCouplingType);
  
  Map<String, Map<String, List<String>>> getInstanceIdVsOtherClassifiers();
  void setInstanceIdVsOtherClassifiers(Map<String, Map<String, List<String>>> instanceIdVsOtherClassifiers);
}
