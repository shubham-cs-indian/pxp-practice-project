package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllVariantContextsResponseModel implements IGetAllVariantContextsResponseModel {
  
  private static final long                               serialVersionUID = 1L;
  protected List<IGridEditVariantContextInformationModel> list;
  protected Long                                          count;
  
  @Override
  public List<IGridEditVariantContextInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = GridEditVariantContextInformationModel.class)
  public void setList(List<IGridEditVariantContextInformationModel> list)
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
