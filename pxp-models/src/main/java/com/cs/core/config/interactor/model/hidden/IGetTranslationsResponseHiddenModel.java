package com.cs.core.config.interactor.model.hidden;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTranslationsResponseHiddenModel extends IModel {
  
  public static final String COUNT = "count";
  public static final String DATA  = "data";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IGetPropertyTranslationsHiddenModel> getData();
  
  public void setData(List<IGetPropertyTranslationsHiddenModel> data);
}
