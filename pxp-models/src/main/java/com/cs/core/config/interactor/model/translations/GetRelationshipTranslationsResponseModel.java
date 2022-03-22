package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetRelationshipTranslationsResponseModel
    implements IGetRelationshipTranslationsResponseModel {
  
  private static final long                        serialVersionUID = 1L;
  protected Long                                   count;
  protected List<IGetRelationshipTranslationModel> data;
  
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
  public List<IGetRelationshipTranslationModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetRelationshipTranslationModel.class)
  public void setData(List<IGetRelationshipTranslationModel> data)
  {
    this.data = data;
  }
}
