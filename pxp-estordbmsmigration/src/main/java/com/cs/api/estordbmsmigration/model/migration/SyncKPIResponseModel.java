package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.config.interactor.model.governancerule.GetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SyncKPIResponseModel implements ISyncKPIResponseModel {

  private static final long serialVersionUID = 1L;

  private Long                               count = 0l;
  private List<IGetKeyPerformanceIndexModel> list  = new ArrayList<>();

  @Override
  public List<IGetKeyPerformanceIndexModel> getList()
  {
    return list;
  }


  @JsonDeserialize(contentAs = GetKeyPerformanceIndexModel.class)
  @Override
  public void setList(List<IGetKeyPerformanceIndexModel> list)
  {
    this.list = list;
  }

  @Override
  public Long getCount()
  {
    return count;
  }


  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
