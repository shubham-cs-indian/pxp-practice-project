package com.cs.api.estordbmsmigration.model.migration;

import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISyncKPIResponseModel extends IModel {


  public static final String LIST = "list";
  public static final String COUNT = "count";

  public void setCount(Long count);

  public Long getCount();

  public List<IGetKeyPerformanceIndexModel> getList();
  public void setList(List<IGetKeyPerformanceIndexModel> list);

}
