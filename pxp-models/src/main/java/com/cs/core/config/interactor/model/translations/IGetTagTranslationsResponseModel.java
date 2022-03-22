package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTagTranslationsResponseModel extends IModel {
  
  public static final String COUNT = "count";
  public static final String DATA  = "data";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IGetTagTranslationsModel> getData();
  
  public void setData(List<IGetTagTranslationsModel> data);
}
