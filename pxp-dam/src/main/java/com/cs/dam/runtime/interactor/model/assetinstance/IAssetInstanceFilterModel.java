package com.cs.dam.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IAssetInstanceFilterModel extends IModel {
  
  public List<IAssetPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<IAssetPropertyInstanceFilterModel> attributes);
  
  public List<IAssetPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<IAssetPropertyInstanceFilterModel> tags);
  
  public String getSortField();
  
  public void setSortField(String sortField);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
}
