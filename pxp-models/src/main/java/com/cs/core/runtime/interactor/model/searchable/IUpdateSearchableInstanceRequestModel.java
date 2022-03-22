package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IUpdateSearchableInstanceRequestModel extends IModel {
  
  public static final String SEARCHABLE_INSTANCE_ID                    = "searchableInstanceId";
  public static final String SEARCHABLE_PROPERTY_INSTANCES_INFORMATION = "searchablePropertyInstancesInformation";
  public static final String LANGUAGE_CODES                            = "languageCodes";
  
  public String getSearchableInstanceId();
  
  public void setSearchableInstanceId(String searchableInstanceId);
  
  public ISearchablePropertyInstancesInformationModel getSearchablePropertyInstancesInformation();
  
  public void setSearchablePropertyInstancesInformation(
      ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
}
