package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;

public interface IGetConfigDetailsForGetNewInstanceTreeRequestModel
    extends IConfigDetailsForInstanceTreeGetRequestModel {
  
  public static final String ATTRIBUTE_IDS           = "attributeIds";
  public static final String TAG_IDS                 = "tagIds";
  public static final String IS_FILTER_DATA_REQUIRED = "isFilterDataRequired";
  
  public List<String> getAttributeIds();
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  public void setTagIds(List<String> tagIds);
  
  public Boolean getIsFilterDataRequired();
  public void setIsFilterDataRequired(Boolean isFilterDataRequired);
  
}
