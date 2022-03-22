package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.endpoint.EndpointBasicInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataIntegrationInfoModel extends EndpointBasicInfoModel
    implements IDataIntegrationInfoModel {
  
  private static final long      serialVersionUID = 1L;
  protected String               mode;
  protected List<ITileInfoModel> data             = new ArrayList<>();
  protected Long                 totalArticlesCount;
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
  @Override
  public List<ITileInfoModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = TileInfoModel.class)
  public void setData(List<ITileInfoModel> data)
  {
    this.data = data;
  }
  
  @Override
  public Long getTotalArticlesCount()
  {
    return totalArticlesCount;
  }
  
  @Override
  public void setTotalArticlesCount(Long totalArticlesCount)
  {
    this.totalArticlesCount = totalArticlesCount;
  }
}
