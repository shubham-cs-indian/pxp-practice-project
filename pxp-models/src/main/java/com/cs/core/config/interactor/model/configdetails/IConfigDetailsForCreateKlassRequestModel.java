package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForCreateKlassRequestModel extends IModel {
  
  public static final String KLASS_ID                         = "klassId";
  public static final String TAXONOMY_IDS                     = "taxonomyIds";
  public static final String SELECTED_TAXONOMY_IDS            = "selectedTaxonomyIds";
  public static final String ORAGANIZATION_ID                 = "organizationId";
  public static final String ENDPOINT_ID                      = "endpointId";
  public static final String PHYSICAL_CATALOG_ID              = "physicalCatalogId";
  public static final String USER_ID                          = "userId";
  public static final String ARTICLE_KLASS_IDS                = "articleKlassIds";
  
  // For Data Flow between Content and Variant
  public static final String PARENT_KLASS_IDS                 = "parentKlassIds";
  public static final String PARENT_TAXONOMY_IDS              = "parentTaxonomyIds";
  public static final String SHOULD_SEND_TAXONOMY_HIERARCHIES = "shouldSendTaxonomyHierarchies";
  public static final String ARTICLE_VARIANT_KLASS_IDS        = "articleVariantklassIds";
  public static final String LINKED_RELATIONSHIP_ID           = "linkedRelationshipId";
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public List<String> getParentKlassIds();
  
  public void setParentKlassIds(List<String> parentKlassIds);
  
  public List<String> getParentTaxonomyIds();
  
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds);
  
  public Boolean getShouldSendTaxonomyHierarchies();
  
  public void setShouldSendTaxonomyHierarchies(Boolean shouldSendTaxonomyHierarchies);
  
  public List<String> getArticleVariantklassIds();
  
  public void setArticleVariantklassIds(List<String> articleVariantklassIds);
  
  public List<String> getArticleKlassIds();
  
  public void setArticleKlassIds(List<String> articleKlassIds);
  
  public String getLinkedRelationshipId();
  
  public void setLinkedRelationshipId(String linkedRelationshipId);
}
