package com.cs.core.runtime.interactor.model.klassinstanceexport;

import com.cs.core.runtime.interactor.model.offboarding.ICustomExportComponentConfigModel;

import java.util.ArrayList;
import java.util.List;

public class CustomExportComponentConfigModel implements ICustomExportComponentConfigModel {
  
  private static final long serialVersionUID      = 1L;
  protected List<String>    pidSkuKlassIds        = new ArrayList<>();
  protected List<String>    singleArticleKlassIds = new ArrayList<>();
  
  @Override
  public List<String> getPidSkuKlassIds()
  {
    return pidSkuKlassIds;
  }
  
  @Override
  public void setPidSkuKlassIds(List<String> pidSkuKlassIds)
  {
    this.pidSkuKlassIds = pidSkuKlassIds;
  }
  
  @Override
  public List<String> getSingleArticleKlassIds()
  {
    return singleArticleKlassIds;
  }
  
  @Override
  public void setSingleArticleKlassIds(List<String> singleArticleKlassIds)
  {
    this.singleArticleKlassIds = singleArticleKlassIds;
  }
}
