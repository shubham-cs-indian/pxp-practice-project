package com.cs.core.config.interactor.model.hidden;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetTranslationsResponseHiddenModel implements IGetTranslationsResponseHiddenModel {
  
  private static final long                           serialVersionUID = 1L;
  protected Long                                      count            = 0L;
  protected List<IGetPropertyTranslationsHiddenModel> data;
  
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
  public List<IGetPropertyTranslationsHiddenModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetPropertyTranslationsHiddenModel.class)
  public void setData(List<IGetPropertyTranslationsHiddenModel> data)
  {
    this.data = data;
  }
}
