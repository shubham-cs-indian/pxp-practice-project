package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.List;

import com.cs.core.config.interactor.model.endpoint.IEndpointBasicInfoModel;

public interface IDataIntegrationInfoModel extends IEndpointBasicInfoModel {
  
  public static final String MODE                 = "mode";
  public static final String DATA                 = "data";
  public static final String TOTAL_ARTICLES_COUNT = "totalArticlesCount";
  
  public String getMode();
  
  public void setMode(String mode);
  
  public List<ITileInfoModel> getData();
  
  public void setData(List<ITileInfoModel> data);
  
  public Long getTotalArticlesCount();
  
  public void setTotalArticlesCount(Long totalArticlesCount);
}
