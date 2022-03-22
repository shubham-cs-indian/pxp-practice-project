package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICustomExportComponentResponseModel extends IModel {
  
  public static String PID_SKU_INSTANCE_IDS        = "pidSkuInstanceIds";
  public static String SINGLE_ARTICLE_INSTANCE_IDS = "singleArticleInstanceIds";
  
  public List<String> getPidSkuInstanceIds();
  
  public void setPidSkuInstanceIds(List<String> pidSkuInstanceIds);
  
  public List<String> getSingleArticleInstanceIds();
  
  public void setSingleArticleInstanceIds(List<String> singleArticleInstanceIds);
}
