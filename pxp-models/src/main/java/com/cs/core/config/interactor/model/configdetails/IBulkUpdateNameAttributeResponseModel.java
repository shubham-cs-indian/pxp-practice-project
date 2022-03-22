package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

public interface IBulkUpdateNameAttributeResponseModel extends IModel {
  
  public static final String UPDATE_SEARCHABLE_DATA = "updateSearchableData";
  
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableData();
  
  public void setUpdateSearchableData(List<IUpdateSearchableInstanceModel> updateSearchableData);
}
