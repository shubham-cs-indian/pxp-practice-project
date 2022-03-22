package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetRequestModel;

public class GetConfigDetailsForGetNewInstanceTreeRequestModel
    extends ConfigDetailsForInstanceTreeGetRequestModel
    implements IGetConfigDetailsForGetNewInstanceTreeRequestModel {
  
  private static final long            serialVersionUID = 1L;
  protected List<String>               attributeIds;
  protected List<String>               tagIds;
  protected Boolean                    isFilterDataRequired = false;
  
  public List<String> getAttributeIds()
  {
    if(attributeIds ==  null) {
      attributeIds = new ArrayList<String>();
    }
    return attributeIds;
  }
  
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  public List<String> getTagIds()
  {
    if(tagIds ==  null) {
      tagIds = new ArrayList<String>();
    }
    return tagIds;
  }
  
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public Boolean getIsFilterDataRequired()
  {
    return isFilterDataRequired;
  }

  @Override
  public void setIsFilterDataRequired(Boolean isFilterDataRequired)
  {
    this.isFilterDataRequired = isFilterDataRequired;
  }
  
}
