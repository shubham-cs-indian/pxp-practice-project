package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetTranslationsResponseModel implements IGetTranslationsResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected Long                                count            = 0L;
  protected List<IGetPropertyTranslationsModel> data;
  
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
  
  @Override
  public List<IGetPropertyTranslationsModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetPropertyTranslationsModel.class)
  public void setData(List<IGetPropertyTranslationsModel> data)
  {
    this.data = data;
  }
}
