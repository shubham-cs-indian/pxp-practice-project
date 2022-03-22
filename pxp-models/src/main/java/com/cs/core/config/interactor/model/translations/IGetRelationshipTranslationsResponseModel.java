package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetRelationshipTranslationsResponseModel extends IModel {
  
  public static final String COUNT = "count";
  public static final String DATA  = "data";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IGetRelationshipTranslationModel> getData();
  
  public void setData(List<IGetRelationshipTranslationModel> data);
}
