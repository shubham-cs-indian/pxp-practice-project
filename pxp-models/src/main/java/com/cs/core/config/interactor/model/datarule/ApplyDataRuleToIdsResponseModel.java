package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ApplyDataRuleToIdsResponseModel implements IApplyDataRuleToIdsResponseModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected List<IUpdateSearchableInstanceModel> updateSearchableData;
  
  @Override
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableData()
  {
    return updateSearchableData;
  }
  
  @Override
  @JsonDeserialize(contentAs = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableData(List<IUpdateSearchableInstanceModel> updateSearchableData)
  {
    this.updateSearchableData = updateSearchableData;
  }
}
