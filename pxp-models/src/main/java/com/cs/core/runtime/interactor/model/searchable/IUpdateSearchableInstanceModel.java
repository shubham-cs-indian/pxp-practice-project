package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IUpdateSearchableInstanceModel extends IModel {
  
  public static final String SEARCHABLE_INSTANCE_IDS                   = "searchableInstanceIds";
  public static final String SEARCHABLE_PROPERTY_INSTANCES_INFORMATION = "searchablePropertyInstancesInformation";
  
  public List<String> getSearchableInstanceIds();
  
  public void setSearchableInstanceIds(List<String> searchableInstanceIds);
  
  public ISearchablePropertyInstancesInformationModel getSearchablePropertyInstancesInformation();
  
  public void setSearchablePropertyInstancesInformation(
      ISearchablePropertyInstancesInformationModel searchablePropertyInstancesInformation);
}
