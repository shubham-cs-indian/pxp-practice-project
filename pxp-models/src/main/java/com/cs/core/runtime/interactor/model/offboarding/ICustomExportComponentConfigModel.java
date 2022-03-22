package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICustomExportComponentConfigModel extends IModel {
  
  public static String PID_SKU_KLASS_IDS        = "pidSkuKlassIds";
  public static String SINGLE_ARTICLE_KLASS_IDS = "singleArticleKlassIds";
  
  public List<String> getPidSkuKlassIds();
  
  public void setPidSkuKlassIds(List<String> pidSkuKlassIds);
  
  public List<String> getSingleArticleKlassIds();
  
  public void setSingleArticleKlassIds(List<String> singleArticleKlassIds);
}
