package com.cs.core.runtime.interactor.model.klassinstanceexport;

import com.cs.core.runtime.interactor.model.offboarding.ICustomExportComponentResponseModel;

import java.util.ArrayList;
import java.util.List;

public class CustomExportComponentResponseModel implements ICustomExportComponentResponseModel {
  
  private static final long serialVersionUID         = 1L;
  protected List<String>    pidSkuInstanceIds        = new ArrayList<>();
  protected List<String>    singleArticleInstanceIds = new ArrayList<>();
  
  @Override
  public List<String> getPidSkuInstanceIds()
  {
    return pidSkuInstanceIds;
  }
  
  @Override
  public void setPidSkuInstanceIds(List<String> pidSkuInstanceIds)
  {
    this.pidSkuInstanceIds = pidSkuInstanceIds;
  }
  
  @Override
  public List<String> getSingleArticleInstanceIds()
  {
    return singleArticleInstanceIds;
  }
  
  @Override
  public void setSingleArticleInstanceIds(List<String> singleArticleInstanceIds)
  {
    this.singleArticleInstanceIds = singleArticleInstanceIds;
  }
}
