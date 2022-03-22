package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.runtime.interactor.model.instance.SearchablePropertyInstancesInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class UpdateSearchableInstanceModel implements IUpdateSearchableInstanceModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation;
  protected List<String>                                 searchableInstanceIds;
  
  @Override
  public List<String> getSearchableInstanceIds()
  {
    if (searchableInstanceIds == null) {
      searchableInstanceIds = new ArrayList<>();
    }
    return searchableInstanceIds;
  }
  
  @Override
  public void setSearchableInstanceIds(List<String> searchableInstanceIds)
  {
    this.searchableInstanceIds = searchableInstanceIds;
  }
  
  @Override
  public ISearchablePropertyInstancesInformationModel getSearchablePropertyInstancesInformation()
  {
    return searchablePropertyInstancesInformation;
  }
  
  @JsonDeserialize(as = SearchablePropertyInstancesInformationModel.class)
  @Override
  public void setSearchablePropertyInstancesInformation(
      ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation)
  {
    this.searchablePropertyInstancesInformation = searchablePropertyInstancesInformation;
  }
}
