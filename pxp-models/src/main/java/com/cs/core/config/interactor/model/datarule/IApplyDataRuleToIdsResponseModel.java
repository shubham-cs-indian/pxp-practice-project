package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;

public interface IApplyDataRuleToIdsResponseModel extends IModel {
  
  public static final String UPDATE_SEARCHABLE_DATA = "updateSearchableData";
  
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableData();
  
  public void setUpdateSearchableData(List<IUpdateSearchableInstanceModel> updateSearchableData);
}
