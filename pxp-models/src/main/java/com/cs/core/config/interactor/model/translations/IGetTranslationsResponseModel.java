package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTranslationsResponseModel extends IModel {
  
  public static final String COUNT = "count";
  public static final String DATA  = "data";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IGetPropertyTranslationsModel> getData();
  
  public void setData(List<IGetPropertyTranslationsModel> data);
}
