package com.cs.core.runtime.interactor.model.searchable;

import java.util.List;

public class UpdateSearchableInstanceRequestModel implements IUpdateSearchableInstanceRequestModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation;
  protected String                                       searchableInstanceId;
  protected List<String>                                 languageCodes;
  
  @Override
  public ISearchablePropertyInstancesInformationModel getSearchablePropertyInstancesInformation()
  {
    return searchablePropertyInstancesInformation;
  }
  
  @Override
  public void setSearchablePropertyInstancesInformation(
      ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation)
  {
    this.searchablePropertyInstancesInformation = searchablePropertyInstancesInformation;
  }
  
  @Override
  public String getSearchableInstanceId()
  {
    return searchableInstanceId;
  }
  
  @Override
  public void setSearchableInstanceId(String searchableInstanceId)
  {
    this.searchableInstanceId = searchableInstanceId;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
}
