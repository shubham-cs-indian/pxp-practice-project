package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel
    implements
    IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          userId;
  protected String          entityId;
  protected String          parentTaxonomyId;
  protected List<String>    leafIds          = new ArrayList<>();
  protected Boolean         isKlassTaxonomy;
  
  public ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel(
      String userId, String entityId, String parentTaxonomyId, List<String> leafIds,
      Boolean isKlassTaxonomy)
  {
    this.userId = userId;
    this.entityId = entityId;
    this.parentTaxonomyId = parentTaxonomyId;
    this.leafIds = leafIds;
    this.isKlassTaxonomy = isKlassTaxonomy;
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
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public List<String> getLeafIds()
  {
    return leafIds;
  }
  
  @Override
  public void setLeafIds(List<String> leafIds)
  {
    this.leafIds = leafIds;
  }
  
  @Override
  public Boolean getIsKlassTaxonomy()
  {
    return isKlassTaxonomy;
  }
  
  @Override
  public void setIsKlassTaxonomy(Boolean isKlassTaxonomy)
  {
    this.isKlassTaxonomy = isKlassTaxonomy;
  }
}
