package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.ArrayList;
import java.util.List;

public class GetEndpointsIdsRequestModel implements IGetEndpointIdsRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  private String            physicalCatalogId;
  private String            userId;
  private String            triggeringType;
  private List<String>      klassIds         = new ArrayList<>();
  private List<String>      taxonomyIds      = new ArrayList<>();
  private List<String>      attributeIds     = new ArrayList<>();
  private List<String>      tagIds           = new ArrayList<>();
  
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
    return this.userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getTriggeringType()
  {
    return triggeringType;
  }
  
  @Override
  public void setTriggeringType(String triggeringType)
  {
    this.triggeringType = triggeringType;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return this.klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return this.taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> selectedAttributes)
  {
    this.attributeIds = selectedAttributes;
  }
  
  @Override
  public List<String> getTagIds()
  {
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> selectedTags)
  {
    this.tagIds = selectedTags;
  }
}
