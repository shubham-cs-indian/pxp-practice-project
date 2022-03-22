package com.cs.core.config.interactor.model.articleimportcomponent;

import java.util.List;

public class KlassInstanceExportAllResponseModel implements IKlassInstanceExportAllResponseModel {
  
  private static final long serialVersionUID = 1L;
  private List<String>      klassInstanceIds;
  private Long              count;
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
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
