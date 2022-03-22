package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForCreateKlassRequestModel
    implements IConfigDetailsForCreateKlassRequestModel {
  
  private static final long serialVersionUID              = 1L;
  protected String          klassId;
  protected List<String>    taxonomyIds;
  protected List<String>    selectedTaxonomyIds;
  protected String          organizationId;
  protected String          endpointId;
  protected String          physicalCatalogId;
  protected String          userId;
  protected List<String>    parentKlassIds;
  protected List<String>    parentTaxonomyIds;
  protected Boolean         shouldSendTaxonomyHierarchies = false;
  protected List<String>    articleVariantklassIds;
  protected List<String>    articleKlassIds;
  protected String          linkedRelationshipId;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
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
  public List<String> getParentKlassIds()
  {
    if (parentKlassIds == null) {
      parentKlassIds = new ArrayList<>();
    }
    return parentKlassIds;
  }
  
  @Override
  public void setParentKlassIds(List<String> parentKlassIds)
  {
    this.parentKlassIds = parentKlassIds;
  }
  
  @Override
  public List<String> getParentTaxonomyIds()
  {
    if (parentTaxonomyIds == null) {
      parentTaxonomyIds = new ArrayList<>();
    }
    return parentTaxonomyIds;
  }
  
  @Override
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds)
  {
    this.parentTaxonomyIds = parentTaxonomyIds;
  }
  
  @Override
  public Boolean getShouldSendTaxonomyHierarchies()
  {
    return shouldSendTaxonomyHierarchies;
  }
  
  @Override
  public void setShouldSendTaxonomyHierarchies(Boolean shouldSendTaxonomyHierarchies)
  {
    this.shouldSendTaxonomyHierarchies = shouldSendTaxonomyHierarchies;
  }
  
  @Override
  public List<String> getArticleVariantklassIds()
  {
    return articleVariantklassIds;
  }
  
  @Override
  public void setArticleVariantklassIds(List<String> articleVariantklassIds)
  {
    this.articleVariantklassIds = articleVariantklassIds;
  }
  
  @Override
  public List<String> getArticleKlassIds()
  {
    if (articleKlassIds == null) {
      return new ArrayList<>();
    }
    return articleKlassIds;
  }
  
  @Override
  public void setArticleKlassIds(List<String> articleKlassIds)
  {
    this.articleKlassIds = articleKlassIds;
  }
  
  @Override
  public String getLinkedRelationshipId()
  {
    return linkedRelationshipId;
  }
  
  @Override
  public void setLinkedRelationshipId(String linkedRelationshipId)
  {
    this.linkedRelationshipId = linkedRelationshipId;
  }
}
