package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllGoldenRecordRulesModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IIdLabelCodeModel> getList();
  
  public void setList(List<IIdLabelCodeModel> list);
}
