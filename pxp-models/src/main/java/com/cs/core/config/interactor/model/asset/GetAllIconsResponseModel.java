package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllIconsResponseModel implements IGetAllIconsResponseModel {
  
  private static final long  serialVersionUID = 1L;
  
  protected List<IIconModel> icons            = new ArrayList<IIconModel>();
  protected Integer          totalCount       =0;
  protected Integer          from             =0;
   
  @Override
  public List<IIconModel> getIcons()
  {
    return icons;
  }
  
  @JsonDeserialize(contentAs = IconModel.class)
  @Override
  public void setIcons(List<IIconModel> icons)
  {
    this.icons = icons;
  }
  
  @Override
  public Integer getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Integer totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
}
