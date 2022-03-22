package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class GetPostConfigDetailsRequestModel implements IGetPostConfigDetailsRequestModel {
  
  private static final long serialVersionUID   = 1L;
  
  protected List<String>    attributeIds;
  protected List<String>    tagIds;
  protected List<String>    klassIds;
  protected Boolean         shouldGetNonNature = true;
  protected String          userId;
  
  @Override
  public Boolean getShouldGetNonNature()
  {
    return shouldGetNonNature;
  }
  
  @Override
  public void setShouldGetNonNature(Boolean shouldGetNonNature)
  {
    this.shouldGetNonNature = shouldGetNonNature;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new ArrayList<>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    if (tagIds == null) {
      tagIds = new ArrayList<>();
    }
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
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
}
